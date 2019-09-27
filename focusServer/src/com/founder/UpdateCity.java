package com.founder;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class UpdateCity {
	
	public static final String DRIVERCLASSNAME = "oracle.jdbc.driver.OracleDriver";
	
//	public static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
//	public static final String USERNAME = "bhfocus";
//	public static final String PASSWORD = "bhfocus";
	
	public static final String URL = "jdbc:oracle:thin:@10.104.0.151:1521:CCDB";
	public static final String USERNAME = "focususer";
	public static final String PASSWORD = "focususer";
   
	public static void main(String[] args) {
		UpdateCity updateCity = new UpdateCity();
		//ִ���޸�OPERATION_CASE_BASE  ORDER_ACCUSE��
		//updateCity.executeUpdate();
		//JdbcTemplate jdbcTemplate = updateCity.getJdbcTemplate();
		
	}
	
	//�������ݿ�
	public JdbcTemplate getJdbcTemplate() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(DRIVERCLASSNAME);
		dataSource.setUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
        try {
			Connection connection = dataSource.getConnection();
			System.out.println(connection.toString());
			System.out.println("���ݿ����ӳɹ�...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ�����ʧ��...");
		}		
        return new JdbcTemplate(dataSource);
	}
	//�������ݿ�
	public JdbcTemplate getJdbcTemplate(String ip) {
		
		String url = "jdbc:oracle:thin:@"+ip+":1521:CCDB";
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(DRIVERCLASSNAME);
		dataSource.setUrl(url);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		try {
			Connection connection = dataSource.getConnection();
			System.out.println(connection.toString());
			System.out.println("���ݿ����ӳɹ�...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ�����ʧ��...");
		}		
		return new JdbcTemplate(dataSource);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getOrderRegion(JdbcTemplate jdbcTemplate) {
		System.out.println("���ڲ�ѯ���ݿ��������...");
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.region_code,t.parent_id from order_region t");
		List<Map<String, String> > list = jdbcTemplate.queryForList(sql.toString());
		Map<String, String> m = new HashMap<String, String>();
		if(null != list){
			for (Map<String, String> map : list) {
				//key �������   value ���򸸱���
				m.put(map.get("REGION_CODE"), map.get("PARENT_ID"));
			}
		}
		return m;
	}
	//�޸�OPERATION_CASE_BASE�� ORDER_ACCUSE��
	public void executeUpdate(String ip){
		JdbcTemplate jdbcTemplate = null;
		if(!"".equals(ip)&&null != ip){
			jdbcTemplate = this.getJdbcTemplate(ip);
		}else{
			jdbcTemplate = this.getJdbcTemplate();
		}
		List<Map<String, String>> exalList = getExalList();
		Map<String, String> orgMap = this.getOrderRegion(jdbcTemplate);
		if(null != exalList){
			System.out.println("���ڸ������Ժ�...");
			int ocbCount = 0;
			int oaCount = 0;
			for (Map<String, String> map : exalList) {
				if(map.get("level").equals("03")){
					String county = map.get("new"); //������
					String city = orgMap.get(county);
					String province = orgMap.get(city);
					//�޸�OPERATION_CASE_BASE��
					StringBuffer sql = new StringBuffer();
					sql.append(" update OPERATION_CASE_BASE t1 set t1.county = ?,t1.city = ?,t1.province = ? " );
					sql.append(" where t1.county = ? " );
					ocbCount += jdbcTemplate.update(sql.toString(), new Object[]{
						county,city,province,map.get("old")
					});
					//�޸�ORDER_ACCUSE��
					StringBuffer sql2 = new StringBuffer();
					sql2.append(" update ORDER_ACCUSE t2 set t2.countyid = ?,t2.areaid = ?,t2.provinceid = ?  " );
					sql2.append(" where t2.countyid = ? " );
					oaCount += jdbcTemplate.update(sql2.toString(), new Object[]{
						county,city,province,map.get("old")
					});
					
				}else{
					String city = map.get("new");
					String province = orgMap.get(city);
					
					StringBuffer sql = new StringBuffer();
					sql.append(" update OPERATION_CASE_BASE t1 set t1.city=?,t1.province=? " );
					sql.append(" where t1.city = ? " );
					ocbCount += jdbcTemplate.update(sql.toString(), new Object[]{
						  city,province,map.get("old")
					});
					//�޸�ORDER_ACCUSE��
					StringBuffer sql2 = new StringBuffer();
					sql2.append(" update ORDER_ACCUSE t2 set t2.areaid = ?,t2.provinceid = ?  " );
					sql2.append(" where t2.areaid = ? " );
					oaCount += jdbcTemplate.update(sql2.toString(), new Object[]{
						city,province,map.get("old")
					});
				}
			}
			System.out.println("OPERATION_CASE_BASE��ɹ�����"+ocbCount+"��");
			System.out.println("ORDER_ACCUSE��ɹ�����"+oaCount+"��");
		}else{
			System.out.println("exal�ļ��쳣...");
		}
		
	}
	
	
	//��ȡexcel ���ؼ���
	public List<Map<String,String>> getExalList(){
		System.out.println("���ڶ�ȡeaxl...");
        ReadExcelUtil readExcelUtil = new ReadExcelUtil();
		File file = new File("D://update.xls");
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			String[][] data = readExcelUtil.getData(file, 0);
			for (int i = 1; i < data.length; i++) {
				Map<String,String> m = new HashMap<String,String>();
				m.put("level", data[i][0]);
				m.put("old", data[i][1]);
				m.put("new", data[i][4]);
				list.add(m);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return list;
	
	}
	
	
}
