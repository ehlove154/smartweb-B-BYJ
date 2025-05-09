package com.yjb.springstudy.controllers;

import com.yjb.springstudy.results.calc.CalculateResult;
import com.yjb.springstudy.services.CalcService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/study")
public class StudyController {
    private final CalcService calcService;

    @Autowired
    public StudyController(CalcService calcService) {
        this.calcService = calcService;
    }


    @RequestMapping(value = "/test-one", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public String getTestOne(@RequestParam(value = "a", required = false, defaultValue = "0") int a,
                             @RequestParam(value = "b", required = false, defaultValue = "0") int b) {
        int total = a + b;
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        return "{\"result\" : " + total + "}";
    }

    @RequestMapping(value = "/calc", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCalc() {
        System.out.println("GET");
        return "study/calc";
    }

    @RequestMapping(value = "/calc", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String postCalc(@RequestParam(value = "a") String aStr,
                           @RequestParam(value = "b") String bStr,
                           @RequestParam(value = "op") String op,
                           Model model) {
        Pair<CalculateResult, Double> resultPair = this.calcService.calculate(aStr, bStr, op);
        if (resultPair.getFirst() == CalculateResult.FAILURE_DIVIDE_BY_ZERO) {
            model.addAttribute("error", "0으로 나눌 수 없습니다.");
        } else if (resultPair.getFirst() == CalculateResult.FAILURE_INVALID_NUMBER) {
            model.addAttribute("error", "올바르지 않은 숫자입니다.");
        } else if (resultPair.getFirst() == CalculateResult.FAILURE_INVALID_OPERATOR) {
            model.addAttribute("error", "올바르지 않은 연산자입니다.");
        } else if (resultPair.getFirst() == CalculateResult.SUCCESS) {
            model.addAttribute("result", resultPair.getSecond());
            model.addAttribute("a", aStr);
            model.addAttribute("b", bStr);
            model.addAttribute("symbol", switch (op) {
                case "plus" -> "+";
                case "minus" -> "-";
                case "multiply" -> "*";
                case "divide" -> "/";
                case "modulo" -> "%";
                case "power" -> "**";
                default -> throw new UnsupportedOperationException();
            });
        } else {
            throw new UnsupportedOperationException();
        }
        return "study/calc";
    }

    @RequestMapping(value = "/calc-xhr", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCalcXhr() {
        return "study/calcXhr";
    }

    @RequestMapping(value = "/calc-xhr", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postCalcXhr(@RequestParam(value = "a") String aStr,
                              @RequestParam(value = "b") String bStr,
                              @RequestParam(value = "op") String op) {
        JSONObject response = new JSONObject();
        Pair<CalculateResult, Double> resultPair = this.calcService.calculate(aStr, bStr, op);
        if (resultPair.getFirst() == CalculateResult.SUCCESS) {
            response.put("value", resultPair.getSecond());
            response.put("symbol", switch (op){
                case "plus" -> "+";
                case "minus" -> "-";
                case "multiply" -> "*";
                case "divide" -> "/";
                case "modulo" -> "%";
                case "power" -> "**";
                default -> throw new UnsupportedOperationException();
            });
        } else {
            response.put("error", switch (resultPair.getFirst()) {
                case FAILURE_DIVIDE_BY_ZERO -> "0으로 나눌 수 없습니다.";
                case FAILURE_INVALID_NUMBER -> "올바르지 않은 숫자입니다.";
                case FAILURE_INVALID_OPERATOR -> "올바르지 않은 연산자입니다.";
                default -> throw new UnsupportedOperationException();
            });
        }
        return response.toString();
    }


//    @RequestMapping(value = "/calc", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
//    public String postCalc(@RequestParam(value = "a") String aStr,
//                           @RequestParam(value = "b") String bStr,
//                           @RequestParam(value = "op") String op,
//                           Model model) {
//        String error = null;
//        Double a = null;
//        Double b = null;
//
//        try {
//            a = Double.parseDouble(aStr);
//            b = Double.parseDouble(bStr);
//        } catch (NumberFormatException e) {
//            error = "올바른 숫자가 아닙니다.";
//        }
//
//        if (error == null) {
//            if((op.equals("divide") || op.equals("modulo")) && b == 0D) {
//                error = "0으로 나눌 수 없습니다.";
//            } else {
//                Double result = switch (op) {
//                    case "plus" -> a + b;
//                    case "minus" -> a - b;
//                    case "multiply" -> a * b;
//                    case "divide" -> a / b;
//                    case "modulo" -> a % b;
//                    case "power" -> Math.pow(a, b);
//                    default -> null;
//                };
//                String symbol = switch (op){
//                    case "plus" -> "+";
//                    case "minus" -> "-";
//                    case "multiply" -> "*";
//                    case "divide" -> "/";
//                    case "modulo" -> "%";
//                    case "power" -> "**";
//                    default -> null;
//                };
//                model.addAttribute("a", a);
//                model.addAttribute("b", b);
//                model.addAttribute("op", op);
//                model.addAttribute("symbol", symbol);
//                model.addAttribute("result", result);
//            }
//        }
//        model.addAttribute("error", error);
//        return "study/calc";
//    }
}
