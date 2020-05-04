package club.sword.community.advice;

import club.sword.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        if (e instanceof CustomizeException){
            model.addAttribute("message", e.getMessage());
        }
        else{
            model.addAttribute("message","服务太热啦，要不然稍等下再来试试~");
        }
        return new ModelAndView("error");
    }
}
