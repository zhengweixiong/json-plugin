package ${selectedPackage};

import lombok.Data;
<#if hasList() == true>
import java.util.List;
</#if>

@Data
public class ${name} {

<#list fieldList as field>
    private ${field.type} ${field.name};
</#list>
}
