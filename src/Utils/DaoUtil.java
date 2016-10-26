package Utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import liwei.ConnectionManager;

@SuppressWarnings("unchecked")
public class DaoUtil {
	private static final String SQL = "sql";
	private static final String LINKED_LIST = "linkedList";
	
	
	
	public static<T> Boolean insert(String tableName,T obj) throws Exception{
		StringBuffer sb = new StringBuffer();
		StringBuffer sb_value = new StringBuffer();
		Map params = new HashMap();
		sb.append("insert into "+tableName+" (");
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().startsWith("_")){
				continue;
			}
			sb.append(StringUtil.convert2DataSourceName(field.getName())+",");
			sb_value.append(":"+StringUtil.convert2DataSourceName(field.getName())+",");
			params.put(StringUtil.convert2DataSourceName(field.getName()), field.get(obj));
		}
		sb = sb.replace(sb.length()-1, sb.length(), ")");
		sb_value = sb.replace(sb_value.length()-1, sb_value.length(), ")");
		sb.append(" values (");
		sb.append(sb_value);
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement psts = null;
		Map sqlAndListMap = DaoUtil.getSqlAndParamList(sb.toString(), params);
		try{
			psts = wrapPreparedStatement(conn, sqlAndListMap);
			int flag = psts.executeUpdate();
			if(flag>0){
				return true;
			}
		}catch (Exception e) {
			throw new RuntimeException("������ݿ�ʧ��");
		}finally{
			if(psts!=null){
				psts.close();
			}
		}
		return false;
	}
	
	
	public static<T> Boolean update(String tableName,T obj,String keyCondition) throws Exception{
		StringBuffer sb = new StringBuffer();
		Map params = new HashMap();
		String str[] = keyCondition.split("=");
		sb.append("update "+tableName+" SET ");
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().startsWith("_")){
				continue;
			}
			sb.append(StringUtil.convert2DataSourceName(field.getName())+" = ");
			sb.append(":"+StringUtil.convert2DataSourceName(field.getName())+" AND ");
			params.put(StringUtil.convert2DataSourceName(field.getName()), field.get(obj));
			if(field.getName().toUpperCase().equals(StringUtil.convert2FieldName(str[0]).toUpperCase())){
				params.put(str[1].substring(1), field.get(obj));
			}
		}
		sb = sb.replace(sb.length()-3, sb.length(), "");
		sb.append(" WHERE "+keyCondition);
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement psts = null;
		Map sqlAndListMap = DaoUtil.getSqlAndParamList(sb.toString(), params);
		try{
			psts = wrapPreparedStatement(conn, sqlAndListMap);
			int flag = psts.executeUpdate();
			if(flag>0){
				return true;
			}
		}catch (Exception e) {
			throw new RuntimeException("������ݿ�ʧ��");
		}finally{
			if(psts!=null){
				psts.close();
			}
		}
		return false;
		}
	
	
	public static<T> T[] query(String tableName,String condition,Map params,Class<T> clazz) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * from "+tableName);
		if(!StringUtil.isEmpty(condition)){
			sb.append(" where " + condition);
		}
		List<T> objList = new ArrayList<T>();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement psts = null;
		ResultSet rs = null;
		Map sqlAndListMap = DaoUtil.getSqlAndParamList(sb.toString(), params);
		try{
			psts = wrapPreparedStatement(conn, sqlAndListMap);
			rs = psts.executeQuery();
			while(rs.next()){
				T obj = clazz.newInstance();
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int i=0;i<rsmd.getColumnCount();i++){
					String columnName = rsmd.getColumnName(i+1);
					Object value = rs.getObject(i+1);
					Field field = clazz.getDeclaredField(StringUtil.convert2FieldName(columnName));
					field.setAccessible(true);
					field.set(obj, value);
					field.setAccessible(false);
				}
				objList.add(obj);
			}
		}catch (Exception e) {
			throw new RuntimeException("��ѯ��ݿ�ʧ��");
		}finally{
			if(psts!=null){
				psts.close();
			}
			if(rs!=null){
				rs.close();
			}
		}
		T[] arrays = (T[])Array.newInstance(clazz, objList.size());
		for (int i = 0; i < arrays.length; i++) {
			arrays[i] = objList.get(i);
		}
		return arrays;
	}
	
	
	public static Map getSqlAndParamList(String sql,Map params) {
		Pattern p = Pattern.compile("[:]\\w*");
		Matcher matcher = p.matcher(sql);
		Map map = new HashMap();
		LinkedList linkedList = new LinkedList();
		while(matcher.find()){
			String matchStr = matcher.group();
			String repStr = matchStr.replaceAll(":", "");
			sql = sql.replaceAll(matchStr, "?");
			linkedList.add(params.get(repStr));
		}
		map.put(SQL, sql);
		map.put(LINKED_LIST, linkedList);
		return map;
	}


	
	public <T> Boolean delete(String tableName,T obj,String keyCondition) throws Exception{
		StringBuffer sb = new StringBuffer();
		Map params = new HashMap();
		String str[] = keyCondition.split("=");
		sb.append("delete from "+tableName);
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().startsWith("_")){
				continue;
			}
			if(field.getName().toUpperCase().equals(StringUtil.convert2FieldName(str[0]).toUpperCase())){
				params.put(str[1].substring(1), field.get(obj));
			}
		}
		sb.append(" WHERE "+keyCondition);
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement psts = null;
		Map sqlAndListMap = DaoUtil.getSqlAndParamList(sb.toString(), params);
		try{
			psts = wrapPreparedStatement(conn, sqlAndListMap);
			int flag = psts.executeUpdate();
			if(flag>0){
				return true;
			}
		}catch (Exception e) {
			throw new RuntimeException("������ݿ�ʧ��");
		}finally{
			if(psts!=null){
				psts.close();
			}
		}
		return false;
	}
	
	private static PreparedStatement wrapPreparedStatement(Connection conn,Map sqlAndListMap) throws SQLException{
		PreparedStatement psts = conn.prepareStatement((String) sqlAndListMap.get(SQL));
		for (int i = 0; i < ((LinkedList)sqlAndListMap.get(LINKED_LIST)).size(); i++) {
			psts.setObject(i+1, ((LinkedList)sqlAndListMap.get(LINKED_LIST)).get(i));
		}
		return psts;
	}
	
	public static Map getTableColumnsAndType(Connection conn,String tableName) throws SQLException{
		Map map = new HashMap();
		String column = null;
		String columnType = null;
		PreparedStatement psts = conn.prepareStatement("desc "+tableName);
		ResultSet rs = psts.executeQuery();
		while(rs.next()){
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i=0;i<rsmd.getColumnCount();i++){
				String columnName = rsmd.getColumnName(i+1);
				if("COLUMN_NAME".equals(columnName)){
					column= (String) rs.getObject(i+1);
				}
				if("COLUMN_TYPE".equals(columnName)){
					columnType= (String) rs.getObject(i+1);
				}
				map.put(column,columnType);
			}
		}
		return map;
	}
}
