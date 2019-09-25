package xyz.wongs.weathertop.tess4j;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassName FileUploadController
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/9/25 16:36
 * @Version 1.0.0
*/
@Controller
public class FileUploadController {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index() {
        return "upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RedirectView singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException, TesseractException {

        byte[] bytes = file.getBytes();
        Path path = Paths.get("F:\\Repertory\\99_WCNGS\\temp/" + file.getOriginalFilename());
        Files.write(path, bytes);

        File convFile = convert(file);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("F:\\Repertory\\99_WCNGS\\tessdata-master");
        tesseract.setLanguage("chi_sim");
        String text = tesseract.doOCR(convFile);
        redirectAttributes.addFlashAttribute("file", file);
        redirectAttributes.addFlashAttribute("text", text);
        return new RedirectView("result");
    }

    @RequestMapping("/result")
    public String result() {
        return "result";
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
