package com.mms.controller;

import com.mms.model.Department;
import com.mms.model.RentRoomDTO;
import com.mms.model.SessionUser;
import com.mms.repository.DepartmentRepository;
import com.mms.service.RoomService;
import com.mms.util.FunctionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/room")
@Slf4j
public class RoomController extends FunctionUtil {

    private Map<String, Object> map;
    private SessionUser sessionUser;
    private final String NAVI = "room";
    private List<Department> departmentList;
    private LocalDate today;

    private RoomService roomService;
    private DepartmentRepository departmentRepository;

    public RoomController(
            RoomService roomService,
            DepartmentRepository departmentRepository
    ) {
        this.roomService = roomService;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/main")
    public String mainView(
            Model model,
            HttpSession session
    ){

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        if(ObjectUtils.isEmpty(sessionUser)){
            return "redirect:/login";
        }

        departmentList = new ArrayList<>();
        departmentList = departmentRepository.findAllByOrderByArrayAsc();

        model.addAttribute("navi", NAVI);
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("sessionUser", sessionUser);

        return "/room/main";
    }

    @PostMapping("/rentRoom")
    @ResponseBody
    public Map<String, Object> rentRoom(
            RentRoomDTO rentRoom,
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        map = roomService.enrollRentRoom(sessionUser, rentRoom);

        return map;
    }

    @PostMapping("/getBookRoomList")
    @ResponseBody
    public Map<String, Object> getBookRoomList(
            HttpSession session
    ){

        map = new HashMap<>();

        sessionUser = new SessionUser();
        sessionUser = (SessionUser) session.getAttribute(SessionName);

        today = LocalDate.now();

        map = roomService.getBookRoomList(today);

        return map;
    }

    @PostMapping("/getRoomCondition")
    @ResponseBody
    public Map<String, Object> getRoomCondition(
            @RequestParam(value = "eventDate", defaultValue = "") String eventDate,
            @RequestParam(value = "seqNo", defaultValue = "") Long seqNo,
            HttpSession session
    ){

        map = new HashMap<>();

        map = roomService.getRoomCondition(eventDate, seqNo);

        return map;
    }

}
