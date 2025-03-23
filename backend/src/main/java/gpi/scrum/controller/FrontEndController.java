import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontEndController {

    @RequestMapping("/api")
    public String foward() {
        return "foward://index.html";
    }

}