package com.lxr.controller;

import com.lxr.pojo.Resume;
import com.lxr.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/4/17
 * @discribe
 */
@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/showAll")
    public String show(Model model){
        List<Resume> allResume = resumeService.findAll();
        model.addAttribute("allResume",allResume);
        return "info";
    }
    @RequestMapping(value = "/updateOne",method = RequestMethod.POST)
    public String updateOne(Resume resume) {
        int i = resumeService.updateOne(resume);
        if(i>0){
            return "redirect:/resume/showAll";
        }else{
            return "update";
        }
    }
    @RequestMapping("/toUpdate")
    public String toUpdate(Model model,long id){
        if(id!=-1){
            Resume resume = resumeService.findById(id);
            model.addAttribute("currResume",resume);
        }
        return "update";
    }

    @RequestMapping("/deleteOne")
    public String deleteOne(long id){
        resumeService.deleteOne(id);
        return "redirect:/resume/showAll";
    }

}
