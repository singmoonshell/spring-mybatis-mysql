package com.bsg.api.controller;

import com.bsg.api.dao.BookDao;
import com.bsg.api.entity.BookEntity;
import com.bsg.api.exception.APIException;
import com.bsg.api.exception.BookNotFoundException;
import com.bsg.api.service.CurdBookService;
import com.bsg.api.util.Page;
import com.bsg.api.util.RespJson;
import com.bsg.api.util.RespJsonFactory;
import com.bsg.api.vo.BookPostVo;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.commons.beanutils.BeanMap;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhang on 2017/4/21. 测试简单的数据库curd 对于书籍的简单的增删改查
 */
@Controller
@RequestMapping("/v1.0/crud")
public class CurdController {
    @Resource
    private CurdBookService curdBookService;

    /**
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RespJson save(HttpServletRequest request, @Valid @RequestBody BookPostVo bookPostVo, Errors errors) throws APIException { // Map<String, Object> param

        if (errors.hasErrors()) {
            System.out.println("输入信息错误");
        }
        Map param = new org.apache.commons.beanutils.BeanMap(bookPostVo);
        RespJson respJson = null;
        System.out.println("bookPostVo" + bookPostVo);
        System.out.println("bookPostVo验证=" + bookPostVo.toString());
        try {
            respJson = curdBookService.save(request, param);
        } catch (Exception e) {
            respJson = RespJsonFactory.buildFailure("书籍新增异常");
            throw new APIException("书籍新增异常");
        }
        return respJson;
    }

    /**
     * @param request
     * @param bookId
     * @return
     * @description 删除
     */
    @RequestMapping(value = "/{bookId}", method = RequestMethod.DELETE)
    @ResponseBody
    public RespJson remove(HttpServletRequest request, @PathVariable("bookId") String bookId) throws BookNotFoundException {
        RespJson respJson = null;
        try {
            respJson = curdBookService.remove(request, bookId);
        } catch (Exception e) {
            respJson = RespJsonFactory.buildFailure("对应书籍不存在");
            throw new BookNotFoundException();
        }
        return respJson;
    }

    /**
     * @param request
     * @param bookId
     * @param param
     * @return
     * @description put请求修改可以在url中添加参数，也可以传递body体
     */
    @RequestMapping(value = "/{bookId}", method = RequestMethod.PUT)
    @ResponseBody
    public RespJson update(HttpServletRequest request, @PathVariable("bookId") String bookId, @RequestBody Map<String, Object> param) throws APIException {
        RespJson respJson = null;
        try {
            param.put("id", bookId);
            respJson = curdBookService.update(request, param);
            return respJson;
        } catch (Exception e) {
            respJson = RespJsonFactory.buildFailure("更新书籍失败");
            throw new BookNotFoundException("No Found Book");
        }

    }

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    /**
     * @param request
     * @param param
     * @return
     * @description 无论是全部查询还是单个查询都可以使用的get接口可以再url后面添加？ key=value&key1=value1对应的param RequestParam 对应的是get请求的参数
     */
    @RequestMapping(value = "mypage",method = RequestMethod.GET)
    @ResponseBody
    public RespJson list(HttpServletRequest request, @RequestParam Map<String, Object> param) throws APIException {
        RespJson respJson = null;
        try {
            List<RespJson> list = new ArrayList<>();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            try {
                BookDao bookDao = sqlSession.getMapper(BookDao.class);
                Page<BookEntity> page = new Page<BookEntity>();
                page.setPageNo(2);
                List<BookEntity> users = bookDao.findPage(page);
                page.setResults(users);
                System.out.println(page);
                respJson = RespJsonFactory.buildSuccess(page);
            } finally {
                sqlSession.close();
            }
        } catch (Exception e) {
            respJson = RespJsonFactory.buildFailure("书籍查询异常");
            throw new APIException("书籍查询异常");
        }
        return respJson;
    }

    /**
     * @param request
     * @param param
     * @return
     * @description 无论是全部查询还是单个查询都可以使用的get接口可以再url后面添加？ key=value&key1=value1对应的param RequestParam 对应的是get请求的参数
     */
    @RequestMapping(value="/page", method = RequestMethod.GET)
    @ResponseBody
    public RespJson page(HttpServletRequest request, @RequestParam Map<String, Object> param) throws APIException {
        RespJson respJson = null;
        try {
            respJson = curdBookService.page(request, param);
        } catch (Exception e) {
            respJson = RespJsonFactory.buildFailure("书籍查询异常");
            throw new APIException("书籍查询异常");
        }
        return respJson;
    }

    /**
     * @param request
     * @param param
     * @return
     * @description 无论是全部查询还是单个查询都可以使用的get接口可以再url后面添加？ key=value&key1=value1对应的param RequestParam 对应的是get请求的参数
     */
    @RequestMapping(value="/excutors", method = RequestMethod.GET)
    @ResponseBody
    @Transactional(rollbackFor = APIException.class)
    public RespJson excutors(HttpServletRequest request, @RequestParam Map<String, Object> param) throws APIException {
        RespJson respJson = null;
        try {
            respJson = curdBookService.excutors(request, param);
            if(1 ==1){
                throw new APIException("书籍查询异常");
            }
        } catch (Exception e) {
            respJson = RespJsonFactory.buildFailure("书籍查询异常");
            throw new APIException("书籍查询异常");
        }
        return respJson;
    }


}
