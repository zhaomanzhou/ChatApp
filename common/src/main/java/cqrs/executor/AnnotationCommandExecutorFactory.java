package cqrs.executor;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AnnotationCommandExecutorFactory implements CommandExecutorFactory
{
    private  Map<Class, CommandExecutor> commandExecutorMap = new HashMap<>();


    //TODO  注解实现
    @Override
    public Map<Class, CommandExecutor> getCommandExecutorMap()
    {
        String rootPath = this.getClass().getClassLoader().getResource("").getPath();


        return null;
    }


    public void walkFileTreeByStream(Path dir) {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
            for(Path path : stream){
                if( Files.isDirectory(path) ) {
                    this.walkFileTreeByStream(path);
                }else{
                    System.out.println(path.getFileName());
                }
            }
            System.out.println(dir.getFileName());
        }catch(IOException e){
            e.printStackTrace();
        }
    }



}
