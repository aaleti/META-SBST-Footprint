package footprints.IO;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class CEFileUtils{
	
	public static String readLine(BufferedReader file){
		String line = null;
		
		try {
			line = file.readLine();

		}catch(IOException e){	
                    System.out.println(e.toString());
		}	
		
		return line;
	}	
	
	public static BufferedReader openFile(String path){
		
		BufferedReader reader = null;
		
		try{
			reader = new BufferedReader(new FileReader(path));
		}catch(IOException e){	
			
		}
		
		return reader;
	}
	
	
	
	public static void deleteFile(String fileName){
		File file = new File(fileName);
		file.delete();
	}
	
	public static void writeFile(String data, String path, String fileName) {
        OutputStream os = null;
        boolean written = false;
        try {
            
        	File directory = new File(path);
        	if(directory.exists() == false){
        		directory.mkdirs();
        	}
        	
        	os = new FileOutputStream(new File(path + fileName));
            os.write(data.getBytes(), 0, data.length());
            written = true;
        } catch (IOException e) {
           System.out.println(e+path);
           if(written){
        	   try {
        		   os.close();
               } catch (Exception e1) {
               	
               }
           }else{
        	   writeFile(data, path, fileName);
           }
        }
    }
	
	public static List<String> extractUsedClasses(String archive){
		BufferedReader file = CEFileUtils.openFile(archive);
		
		List<String> usedClassesList = new ArrayList<String>();
		
		if(file != null){
		
			String line = CEFileUtils.readLine(file);
			
			while(line != null){
				usedClassesList.add(line.trim());
				line = CEFileUtils.readLine(file);
			}
		}	
		
		return usedClassesList;
	}
	
	public static String findFile(String name,File file){
        File[] list = file.listFiles();
        String filePath = null;
        if(list!=null)
        for (File fil : list){
            if (fil.isDirectory()){
            	filePath = findFile(name,fil);
            }
            else if (name.equalsIgnoreCase(fil.getName())){
                return fil.getParentFile().toString();
            }
        }
        
        return filePath;
    }
	
	private static void unzipJar(String jarFullPath, String jarFile) throws IOException{
		java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFullPath);
		
		File fileDest = new File(System.getProperty("user.dir") + "/classes/" + jarFile.replace(".jar", "") + "/");
		if(fileDest.exists() == false){
			fileDest.mkdirs();
		}
		
		java.util.Enumeration enumEntries = jar.entries();
		while (enumEntries.hasMoreElements()) {
		    java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
		    java.io.File f = new java.io.File(fileDest + java.io.File.separator + file.getName());
		    if (file.isDirectory()) { // if its a directory, create it
		        f.getParentFile().mkdirs();
		        continue;
		    }
		    
		    if(f.getParentFile().exists() == false){
		    	f.getParentFile().mkdirs();
		    }
		    
		    java.io.InputStream is = jar.getInputStream(file); // get the input stream
		    java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
		    while (is.available() > 0) {  // write contents of 'is' to 'fos'
		        fos.write(is.read());
		    }
		    fos.close();
		    is.close();
		}
	}	
	
	public static void copyFiles(String src, String dst, String fileType) throws IOException{
		
		File folder = new File(src);
		
		String [] files = folder.list();
		
		if(files == null){
			return;
		}
		
		for(String file : files) {
			
			if(file.contains(fileType)){
				
				File newDst = new File(String.valueOf(dst));
				if(newDst.exists() == false){
					newDst.mkdirs();
				}
				
				Files.copy(new File(src + "/" + file).toPath(),
					        (new File(newDst + "/" + file)).toPath(),
					        StandardCopyOption.REPLACE_EXISTING);
			}else if(new File(src + "/" + file).isDirectory()){
				copyFiles(src + "/" + file, dst + "/" + file, fileType);
			}
		}
	}
	
	public static void validateFolder(String folder){
		File folderCompiling = new File(folder);
		if(folderCompiling.exists() == false){
			folderCompiling.mkdir();
		}
	}
	
	public static String getIOString(InputStream io) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(io));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ( (line = reader.readLine()) != null) {
		   builder.append(line);
		   builder.append(System.getProperty("line.separator"));
		}
		return builder.toString();
	}
	
	public static void saveBufferedImage(String outputFile, BufferedImage image){
		File outputfile = new File(outputFile);
		try{
			ImageIO.write(image, "jpg", outputfile);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
}