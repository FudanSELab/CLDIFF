public class StringBuilderCase {
	public String main(String s){
		final StringBuilder sb = new StringBuilder();
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
