package com.maryun.common.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.maryun.lb.mapper.SensitiveWords.SensitiveWordsMapper;
import com.maryun.model.PageData;
import com.maryun.utils.WebResult;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KeywordFilter 
{
	//@Value("${system.sensitive.words.key}")
	private String sensitiveKey="SENSITIVEWORDS_BINARYTREE";
	@Autowired
	private SensitiveWordsMapper sensitiveWordsMapper;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
   
    private static HashMap keysMap=new HashMap();
    private static int matchType = 2;            // 1:最小长度匹配 2：最大长度匹配
    private static String[] patternStrs={"","\\d|\\w|[\u4e00-\u9fa5]","\\w|[\u4e00-\u9fa5]","[\u4e00-\u9fa5]"};//正则匹配
    
    /**
     * 初始化敏感词列表
     * */
    private void initfiltercode()
    {
    	ValueOperations<String, Object> redisOpt = redisTemplate.opsForValue();
    	if(redisTemplate.hasKey(sensitiveKey)){
    		keysMap=(HashMap)redisOpt.get(sensitiveKey);
    	}else{
    		addKeywords(getAllProperties());//构建二叉树
    		redisOpt.set(sensitiveKey, keysMap);
    	}
    }
    /**
     * 集合构建成二叉树
     * @param keywords
     */
    private void addKeywords(List<String> keywords)
    {
        for (int i = 0; i < keywords.size(); i++)
        {
            String key = keywords.get(i).trim();
            HashMap nowhash = null;
            nowhash = keysMap;
            for (int j = 0; j < key.length(); j++)
            {
                char word = key.charAt(j);
                Object wordMap = nowhash.get(word);
                if (wordMap != null)
                {
                    nowhash = (HashMap) wordMap;
                } else
                {
                    HashMap<String, String> newWordHash = new HashMap<String, String>();
                    newWordHash.put("isEnd", "0");
                    nowhash.put(word, newWordHash);
                    nowhash = newWordHash;
                }
                if (j == key.length() - 1)
                {
                    nowhash.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 检查一个字符串从begin位置起开始是否有keyword符合， 如果有符合的keyword值，返回值为匹配keyword的长度，否则返回零
     * flag 1:最小长度匹配 2：最大长度匹配
     */
    private int checkKeyWords(String txt, int begin, int flag)
    {
        HashMap nowhash = keysMap;
        int maxMatchRes = 0;
        int res = 0;
        int l = txt.length();
        char word = 0;
        for (int i = begin; i < l; i++)
        {
            word = txt.charAt(i);
            Object wordMap = nowhash.get(word);
            if (wordMap != null)
            {
                res++;
                nowhash = (HashMap) wordMap;
                if (((String) nowhash.get("isEnd")).equals("1"))
                {
                    if (flag == 1)
                    {
                        wordMap = null;
                        nowhash = null;
                        txt = null;
                        return res;
                    } else
                    {
                        maxMatchRes = res;
                    }
                }
            } else
            {
                txt = null;
                nowhash = null;
                return maxMatchRes;
            }
        }
        txt = null;
        nowhash = null;
        return maxMatchRes;
    }
   
    /**
     *  检查一个字符串从begin位置起开始是否有keyword符合， 如果有符合的keyword值，返回值为匹配keyword的长度，否则返回零
     * @param txt 过滤文字
     * @param begin 开始字符
     * @param flag 1:最小长度匹配 2：最大长度匹配
     * @param patternStr 正则规则
     * @return
     */
    private int checkKeyWords(String txt, int begin, int flag,String patternStr)
    {
    	HashMap nowhash = null;
    	nowhash = keysMap;
    	int maxMatchRes = 0;
    	int res = 0;
    	int l = txt.length();
    	char word = 0;
    	for (int i = begin; i < l; i++)
    	{
    		word = txt.charAt(i);
    		boolean pattern = Pattern.matches(patternStr,String.valueOf(word));
    		if(!pattern){
    			if(res==0){
    				txt = null;
    				nowhash = null;
    				return maxMatchRes;
    			}else{
    				res++;
    			}
    			continue;
    		}
    		Object wordMap = nowhash.get(word);
    		if (wordMap != null)
    		{
    			res++;
    			nowhash = (HashMap) wordMap;
    			if (((String) nowhash.get("isEnd")).equals("1"))
    			{
    				if (flag == 1)
    				{
    					wordMap = null;
    					nowhash = null;
    					txt = null;
    					return res;
    				} else
    				{
    					maxMatchRes = res;
    				}
    			}
    		} else
    		{
    			txt = null;
    			nowhash = null;
    			return maxMatchRes;
    		}
    	}
    	txt = null;
    	nowhash = null;
    	return maxMatchRes;
    }

    /**
     * 返回txt中关键字的列表
     */
    private List<String> getTxtKeyWords(String txt)
    {
        List<String> list = new ArrayList<String>();
        int l = txt.length();
        for (int i = 0; i < l;)
        {
            int len = checkKeyWords(txt, i, matchType);
            if (len > 0)
            {
                String tt =   txt.substring(i, i + len);
                list.add(tt);
                i += len;
            } else
            {
                i++;
            }
        }
        txt = null;
        return list;
    }
    /**
     * 关键词替换
     * @param keyworkList
     * @param replaceStr 替换字符串  
     * @return
     */
    private List<String> replaceKeyWords(List<String> keyworkList,String replaceStr)
    {
    	List<String> list = new ArrayList<String>();
    	for (String txt : keyworkList) {
        	list.add(replaceKeyWords(txt, replaceStr));
		}
    	return list;
    }
    /**
     * 关键词替换
     * @param keyworkStr 
     * @param replaceStr 替换字符串  
     * @return
     */
    private String replaceKeyWords(String keyworkStr,String replaceStr){
    		int l = keyworkStr.length();
    		for (String patternStr : patternStrs) {
    			for (int i = 0; i < l;){
    				int len = checkKeyWords(keyworkStr, i, matchType,patternStr);
    				if (len > 0)
    				{
    					String tt =   keyworkStr.substring(i, i + len);
    					keyworkStr=keyworkStr.replaceAll(tt,tt.replaceAll(patternStr,replaceStr));
    					i += len;
    				} else
    				{
    					i++;
    				}
    				
    			}
    		}
    	return keyworkStr;
    }
    
  
    /**
     * 仅判断txt中是否有关键字
     */
    private boolean isContentKeyWords(String txt)
    {
        for (int i = 0; i < txt.length(); i++)
        {
            int len = checkKeyWords(txt, i, 1);
            if (len > 0)
            {
                return true;
            }
        }
        txt = null;
        return false;
    }
    
    /**
     * 文字过滤
     * @param word
     * @param replaceChar
     * @return
     */
    private String replaceKeyWord(String word,String replaceChar){
    	initfiltercode();
    	return replaceKeyWords(word,replaceChar==null?"":replaceChar);
    }
    /**
     * 文字过滤
     * @param word list集合
     * @param replaceChar
     * @return
     */
    private List<String> replaceKeyWord(List<String> word,String replaceChar){
    	return replaceKeyWords(word,replaceChar==null?"":replaceChar);
    }
    
   
 
    /**
     *查询数据库
     * @return
     */
    private List<String> getAllProperties(){
    	List<String> selectPdList = sensitiveWordsMapper.selectSensitiveWords(null);
      return selectPdList;
//    	List<String> selectPdList =new ArrayList<String>();
//    	selectPdList.add("中国");
//    	return selectPdList;
    }
    
    /**
     * 
     * @Description: 校验敏感词
     * @param pd----->words字段
     * @return    
     * @return PageData    
     * @throws
     */
    public PageData checkWords(PageData pd){
    	String words=pd.getString("words");
    	if(StringUtils.isNotBlank(words)){
    		initfiltercode();
    		//检查是否有敏感词，如果有 返回true
    		boolean isContent=this.isContentKeyWords(words);
    		if(isContent){
    			return WebResult.requestFailed(400, "有敏感词",null);
    		}else{
    			return WebResult.requestSuccess();
    		}
    	}else{
    		return WebResult.requestSuccess();
    	}
    }
    
    
//    public static void main(String[] args) throws IOException
//    {
//    	
//    	KeywordFilter filter=new KeywordFilter();
//    	filter.initfiltercode();
//    	 Integer a = 1;
//         // 先垃圾回收
//         System.gc();
//         long start = Runtime.getRuntime().freeMemory();
//    	for (int i = 0; i < 1; i++) {
//    		Date date = new Date();
//    	      String txt = "孩子……习...aaaa..”近 0  中国  平长孙湘雨闻言忍俊不禁，习近平用手中的折扇掩着嘴，布卖淫女止不住地笑了起来，直到谢安脸上不渝的表情越来越明显，她这才逐渐收起笑意，轻笑着说道，“呐，舞姐姐本来就是做事细致的人，似你等懒散，她瞧得过去才怪！——更别说你还背着她到城里的青楼吃酒，";
//    	      boolean boo = filter.isContentKeyWords(txt);
//    	      Date initDate = new Date();
//    	        System.out.println(boo);
//    	        List<String> set = filter.getTxtKeyWords(txt);
//    	        System.out.println("包含的敏感词如下："+set);
//    	        Date date2 = new Date();
//    	        float ss = date2.getTime() - date.getTime();
//    	        float initss = initDate.getTime() - date.getTime();
//    	        float filterss = date2.getTime() - initDate.getTime();
//    	        System.out.println("总耗时"+ss + "毫秒");
//    	        System.out.println("文件读取"+initss + "毫秒");
//    	        System.out.println("过滤"+filterss + "毫秒");
//    	         Date repDate=new Date();
//    	        System.out.println(replaceKeyWord(txt, "*"));
//    	        List<String> strArr=new ArrayList<String>();
//    	        strArr.add(txt);
//    	        strArr.add(txt);
//    	        strArr.add(txt);
//    	        List<String> strArrWorld=replaceKeyWord(strArr, "*");
//    	        for (String string : strArrWorld) {
//					System.out.println(string);
//				}
//    	        Date repDate2=new Date();
//    	        System.out.println("文字替换耗时:"+(repDate2.getTime()-date.getTime())+"毫秒");
//		}
//		// 快要计算的时,再清理一次
//		//        System.gc();
//    	long end = Runtime.getRuntime().freeMemory();
//        System.out.println("一个HashMap对象占内存:" + (end - start));
//    }
    
    
  

    }


