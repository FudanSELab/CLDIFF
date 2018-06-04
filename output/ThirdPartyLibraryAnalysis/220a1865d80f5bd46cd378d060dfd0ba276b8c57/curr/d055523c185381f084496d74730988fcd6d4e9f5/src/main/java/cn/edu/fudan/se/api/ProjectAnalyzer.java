package cn.edu.fudan.se.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import cn.edu.fudan.se.ast.AstParser;
import cn.edu.fudan.se.db.DB;
import javassist.NotFoundException;

public class ProjectAnalyzer {
//	private List exprList = new ArrayList<>();
//	public List getExprList() {
//		return exprList;
//	}
	
	private int cut;
	private StringBuilder typeString;
	
	public List analyseOneFile(String path,CombinedTypeSolver typeSolver) {
		List calls = new ArrayList<>();
//		TypeSolver[] solvers = createJarTypeSolverList(getLibPathUsedByProj(projectId));
		CompilationUnit cu = AstParser.getCompilationUnit(path);
		calls = getCallExpr(cu,typeSolver);
		return calls;
	}
		
//	private List getLibPathUsedByProj(int projectId) {
//		List libPaths = new ArrayList<>();
//		ResultSet rs = DB.query("SELECT * FROM `project_lib_usage` where `project_id`=" + projectId);
//		try {
//			while (rs.next()) {
//				int versionTypeId = rs.getInt("version_type_id");
//				ResultSet trs = DB.query("SELECT * FROM `version_types` where `type_id`=" + versionTypeId);
//				while (trs.next()) {
//					String packageUrl = trs.getString("jar_package_url");
//					if(packageUrl.endsWith(".jar"))
//						libPaths.add("F:/GP/lib/"+packageUrl);
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return libPaths;
//	}
	
	public static TypeSolver[] createJarTypeSolverList(List libPaths,List contextPaths) {
		List solvers = new ArrayList<>();
		solvers.add(new ReflectionTypeSolver());	
		for(String context:contextPaths)
			solvers.add(new JavaParserTypeSolver(new File(context)));
//		solvers.add(new JavaParserTypeSolver(new File("F:\\GP\\high_quality_repos\\xetorthio\\jedis\\src\\main\\java")));
		for(String path:libPaths) {
			try {
				solvers.add(JarTypeSolver.getJarTypeSolver(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		TypeSolver[] result = {};
		return solvers.toArray(result);
	}
	
	
//	public List getCallExpr(CompilationUnit cu,TypeSolver[] solvers) throws IOException {
	public List getCallExpr(CompilationUnit cu,CombinedTypeSolver typeSolver){
		List exprList = new ArrayList<>();
//		TypeSolver myTypeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(),
//				JarTypeSolver.getJarTypeSolver("jars/library1.jar"),
//				JarTypeSolver.getJarTypeSolver("jars/library2.jar"),
//				JarTypeSolver.getJarTypeSolver("jars/library3.jar"),
//				new JavaParserTypeSolver(new File("src/main/java")),
//				new JavaParserTypeSolver(new File("generated_code")));	
		
//		CombinedTypeSolver typeSolver = new CombinedTypeSolver(solvers);

		List fieldAccessExpr = Navigator.findAllNodesOfGivenClass(cu, FieldAccessExpr.class);
		List methodCalls = Navigator.findAllNodesOfGivenClass(cu, MethodCallExpr.class);
		List classOrInterfaceType = Navigator.findAllNodesOfGivenClass(cu, ClassOrInterfaceType.class);
		List explicitConstructorInvocationStmt = Navigator.findAllNodesOfGivenClass(cu, ExplicitConstructorInvocationStmt.class);
		methodCalls.forEach(mc -> {
//			System.out.println(mc.getNameAsString());
			try {
				ResolvedMethodDeclaration rmd= JavaParserFacade.get(typeSolver).solve(mc).getCorrespondingDeclaration();
				String signature = rmd.getSignature();				
				int numberOfParams = rmd.getNumberOfParams();
				for(int i=0;i { //包含构造方法调用
			String type = ci.getNameAsString();
			exprList.add(type);
		}); 
		fieldAccessExpr.forEach(fae -> { 
			String field = fae.getNameAsString();
			exprList.add(field);
		}); 
		explicitConstructorInvocationStmt.forEach(ecis -> {
			try {
				ResolvedConstructorDeclaration rmd= JavaParserFacade.get(typeSolver).solve(ecis).getCorrespondingDeclaration();
				String signature = rmd.getSignature();
//				String call = JavaParserFacade.get(typeSolver).solve(ecis).getCorrespondingDeclaration().getQualifiedSignature();
				int numberOfParams = rmd.getNumberOfParams();
				for(int i=0;i children = type.getChildNodes();
		for(int c=0;c= this.typeString.length()) || ((end+1 < this.typeString.length())&& this.typeString.charAt(end+1) != '.')) {
						this.typeString.replace(start-1, end+1, "");
						this.cut += (end - start + 2);
					}					
				}					
			}
			else
				deleteTypeQualifier(children.get(c));
		}		
	}
	
	public void test() {
//		Type ty = JavaParser.parseType("java.lang.Class...");
		
//		"java.lang.Class... aa"
		Parameter ty = JavaParser.parseParameter("java.lang.Object... aa");
//		JavaParser.parse("registerImplementation(java.lang.Class)");
//		System.out.println(ty);
		cut = 0;
		// "java.lang.Class[]"
		this.typeString = new StringBuilder("java.lang.Object... aa");
		deleteTypeQualifier(ty);
//		System.out.println(this.typeString.toString());
	}
}
