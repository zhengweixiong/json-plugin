package org.zwx.idea.right.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.template.Template;
import org.zwx.idea.right.model.NewRightModel;

import java.io.StringWriter;

/**
 * @author zwx
 */
public class PisFileByFreeMarkerUtil {

    public static FreemarkerConfiguration freemarker = new FreemarkerConfiguration("/templates");

    /**
     * 右键创建扩展点
     * @param project
     * @param model
     * @param moduleRootPath
     * @throws Exception
     */
    public static void createFile(Project project, Object model, String moduleRootPath,String ftlName) throws Exception {

        NewRightModel rightModel =(NewRightModel)model;
        rightModel.initClassList();
        VirtualFile vf = createPackageDir(rightModel.getSelectedPackage(),moduleRootPath);
        //可以使用virtualfile.createChildData（）方法创建文件实例
        for (NewRightModel.Class c : rightModel.getClassList()) {
            VirtualFile virtualFile=vf.createChildData(project, c.getName()+".java");
            StringWriter sw = new StringWriter();
            Template template = PisFileByFreeMarkerUtil.freemarker.getTemplate(ftlName);
            template.process(c, sw);
            //使用VirtualFile.setBinaryContent()写一些数据到对应的文件中
            virtualFile.setBinaryContent(sw.toString().getBytes(IdeaUtils.DEFAULT_CHARSET));
        }

    }



    private static VirtualFile createPackageDir(String packageName, String moduleRootPath) {
        packageName = "src/main/java/" + packageName;
        String path = FileUtil.toSystemIndependentName(moduleRootPath + "/" + StringUtil.replace(packageName, ".", "/"));
        //new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }




}
