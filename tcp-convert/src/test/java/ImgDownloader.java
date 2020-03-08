import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImgDownloader
{
    public static void main(String[] args) throws IOException
    {
        String url = "https://i5.mmzztt.com/2018/03/16c";
        String savePath = "E:\\workspace\\workspace_idea\\ChatApp\\img\\img2\\";
        int imgNum = 64;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        File f = new File(savePath);
        if(!f.exists())
            f.mkdir();
        for(int i = 1; i <= imgNum; i++)
        {
            String item = i < 10? "0" + i: i+ "";
            HttpGet httpGet = new HttpGet(url+item+".jpg");
            httpGet.setHeader("Host", "i5.mmzztt.com");
            httpGet.setHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
            httpGet.setHeader("Referer", "https://www.mzitu.com/205668/"+ item);
            httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println(i + " response " + response.getStatusLine()) ;
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200  && entity != null ) {
                System.out.println("Status code 200");
                InputStream inputStream = entity.getContent();
                FileOutputStream output = new FileOutputStream( savePath+ i + ".jpg");
                IOUtils.copy(inputStream, output);
                inputStream.close();
                output.close();
                response.close();
            }
            System.out.println("The " + i + " is completed");
        }
    }

    public static void main2(String[] args) throws IOException
    {

        File file = new File("append.txt");
        if(!file.exists())
            file.createNewFile();
        CloseableHttpClient httpClient = HttpClients.createDefault();


        for(int i = 1; i <= 80; i++)
        {
            HttpGet httpGet = new HttpGet("http://labexam.xjtu.edu.cn/redir.php?catalog_id=6&cmd=learning&tikubh=1471&page=" + i);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String content;
            if (response.getStatusLine().getStatusCode() == 200)
            {
                //请求体内容
                content = EntityUtils.toString(response.getEntity(), "GBK");
                //内容写入文件

                Document doc= Jsoup.parse(content);
                Elements postItemElements=doc.getElementsByClass("shiti-content"); // 根据样式名称来查询DOM
                for(Element e:postItemElements){
                    System.out.println(e.toString());

                    try {
                        FileUtils.write(file, e + "\r\n", "GBK", true);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }


            }
            response.close();
        }

    }
}
