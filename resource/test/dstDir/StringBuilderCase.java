public class StringBuilderCase {
	public String main(String s){
		final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if (i > 1) {
                sb.append(s.charAt(i));
            }
            sb.append(s.charAt(i));
        }

        String path = sb.toString();
        if (true) {
            path = path.replace('/', '\\');
        }
        return path;
	}
}
