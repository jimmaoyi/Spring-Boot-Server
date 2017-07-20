package com.maryun.restful;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.maryun.restful.base.BaseRestful;

//@RestController
//@RequestMapping("/users1111")
public class UserController extends BaseRestful {

//	@Autowired
//	private UserService userService;
//	private static AtomicInteger count = new AtomicInteger(0);//线程安全的计数变量 
//
//	/**
//	 * 根据条件查询数据
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/findUserInfo", method=RequestMethod.POST)
//	public Object findUserInfo(Map<String,Object> params) {
//		System.out.println(count.incrementAndGet());
//		// 查询数据集合
//		User user=new User();
//		List<User> lists_user = userService.getLists(user);
//		return lists_user;
//	}
//	
//	 /**
//		 * 保存添加操作
//		 * 
//		 * @return
//		 */
//		@RequestMapping(value = "/addUser", method=RequestMethod.POST)
//		public String addUser(@RequestBody Map map) {
//		    User user=new User();
//		    BeanUtil.transMap2Bean2(map, user);
//			userService.insertTest(map);
//			return "success";
//		}
//		@RequestMapping(value = "/deleteUser", method=RequestMethod.DELETE)
//		public String deleteAllUser(){
//			userService.deleteTest();
//			return "success";
//		}
//
//	/**
//	 * 保存添加操作
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/add", method=RequestMethod.POST)
//	public String postUser( User user) {
//			userService.save(user);
//		return "success";
//	}
//	/**
//	 * 获取单个
//	 * @param id
//	 * @return
//	 */
//	@RequestMapping(value="get/{id}", method=RequestMethod.GET) 
//    public User getUser(@PathVariable Integer id) { 
//        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息 
//        // url中的id可通过@PathVariable绑定到函数的参数中 
//		 return userService.getById(id);
//    } 
//	
//	/**
//	 * 删除
//	 * @param id
//	 * @return
//	 */
//	 @RequestMapping(value="delete", method=RequestMethod.DELETE) 
//	 public String deleteUser(Integer[] ids) { 
//		 for (Integer id:ids) {
//			 userService.deleteById(id);
//		}
//	    return "success"; 
//	  }

}