package norm.naming;

/**
 * 下划线命名策略，将类名转换为下划线形式，例如UserInfo -&gt; user_info
 */
public class UnderLineTableNameStrategy implements TableNameStrategy{

    @Override
    public String format(String className) {
        StringBuilder sb = new StringBuilder(className.length() + 6);
        for(int i=0;i<className.length();i++){
            char c = className.charAt(i);
            if(Character.isUpperCase(c)){
                if(i > 0){
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
