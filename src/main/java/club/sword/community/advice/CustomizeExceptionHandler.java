package club.sword.community.advice;

import club.sword.community.dto.ResultDTO;
import club.sword.community.exception.CustomizeErrorCode;
import club.sword.community.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        String contentType = request.getContentType();
        //实现若contentType为application/json，则"返回json"的错误信息
        //若不是，则"跳转错误页面"
        if ("application/json".equals(contentType)) {
            // 返回 JSON
            ResultDTO resultDTO;
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                //书写返回结果
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        }
        else{
            // 错误页面跳转
            if (e instanceof CustomizeException){
                //抛出的异常是"自定义"的异常
                model.addAttribute("message", e.getMessage());
            }
            else{
                //抛出的异常不是"自定义"的异常，则显示“服务冒烟了，要不然你稍后再试试！！！”
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
