package edu.fdu.se.astdiff.miningoperationbean.base;

/**
 * Created by huangkaifeng on 2018/3/27.
 *
 */
public class ChangeEntityDesc {

    public static class StageIIIOpt{

        public static final String OPT_INSERT = "Insert";

        public static final String OPT_MOVE = "Move";

        public static final String OPT_DELETE = "Delete";

        public static final String OPT_CHANGE = "Change";
    }

    public static class StageIIENTITY {

        public static final String ENTITY_CLASS = "Inner Class";

        public static final String ENTITY_INNER_CLASS = "Class";

        public static final String ENTITY_INTERFACE = "Interface";

        public static final String ENTITY_FIELD = "Field";

        public static final String ENTITY_ENUM = "Enum";

        public static final String ENTITY_INITIALIZER = "Initializer";

        public static final String ENTITY_METHOD = "Method";

        public static final String ENTITY_ASSERT = "Assert";

        public static final String ENTITY_BREAK = "Break";

        public static final String ENTITY_CONTINUE = "Continue";

        public static final String ENTITY_EXPRESSION_STMT = "Expression Statement";

        public static final String ENTITY_FOR_STMT = "For Statement";

        public static final String ENTITY_IF_STMT = "If Statement";

        public static final String ENTITY_RETURN_STMT = "Return Statement";

        public static final String ENTITY_SWITCH_STMT = "Switch Statement";

        public static final String ENTITY_SYNCHRONIZED_STMT = "Synchronized Statement";

        public static final String ENTITY_TRY_STMT = "Try Statement";

        public static final String ENTITY_VARIABLE_STMT = "Variable Declaration Statement";

        public static final String ENTITY_WHILE_STMT = "While Statment";

        public static final String ENTITY_DO_STMT = "Do statement";

        public static final String ENTITY_EMPTY_STMT = "Empty Statement";

        public static final String ENTITY_LABELED_STMT = "Labeled Statement";

        public static final String ENTITY_THROW_STMT = "Throw Statement";

        public static final String ENTITY_ENHANCED_FOR_STMT = "Enhanced For Statement";

        public static final String ENTITY_TYPE_DECLARATION_STMT = "Type Declaration Statement";

    }

    public class StageIIIOpt2{

        public static final String OPT2_INSERT = "Insert";

        public static final String OPT2_DELETE = "Delete";

        public static final String OPT2_MOVE = "Move";

        public static final String OPT2_UPDATE = "Update";

        public static final String OPT2_CHANGE = "Change";

    }

    public class StageIIISub{
        /**
         * 针对类似于if结构
         */
        public static final String SUB_CONDITION = "Condition";

        public static final String SUB_CONDITION_AND_BODY = "Condition and Body";

        /**
         * 针对class
         */

        public static final String SUB_SIGNATURE = "Signature";

        public static final String SUB_SIGNATURE_AND_BODY = "Signature and body";

        /**
         * Try
         */
        public static final String SUB_BODY_AND_CATCH_CLAUSE = "Body and Catch Clause";

        public static final String SUB_BODY_AND_CATCH_CLAUSE_AND_FINALLY = "Body and Catch Clause and Finally";


        /**
         * switch
         */

    }


    public class StageIIGranularity{
        public static final String GRANULARITY_MEMBER = "Member";

        public static final String GRANULARITY_CLASS = "ClassOrInterface";

        public static final String GRANULARITY_STATEMENT = "Statement";
    }




    public class StageIIGenStage{

        public static final String ENTITY_GENERATION_STAGE_PRE_DIFF = "PRE_DIFF";
        public static final String ENTITY_GENERATION_STAGE_GT_UD = "UP_DOWN";
        public static final String ENTITY_GENERATION_STAGE_GT_DUD = "DOWN_UP_DOWN";
    }










}
