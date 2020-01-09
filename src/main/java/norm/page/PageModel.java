package norm.page;

import norm.core.parameter.Parameter;
import norm.core.parameter.ValueParameter;

import java.util.List;

public interface PageModel{

    String getSql();

    ValueParameter getFirstParameter();

    ValueParameter getSecondParameter();
}
