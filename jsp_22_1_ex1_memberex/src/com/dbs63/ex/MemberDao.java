package com.dbs63.ex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDao {
	public static final int MEMBER_NONEXISTENT = 0;
	public static final int MEMBER_EXISTENT = 1;
	public static final int	MEMBER_JOIN_FAIL = 0;
	public static final int MEMBER_JOIN_SUCCESS = 1;
	public static final int MEMBER_LOGIN_PW_NO_GOOD = 0;
	public static final int	MEMBER_LOGIN_SUCCESS = 1;
	public static final int MEMBER_LOGIN_IS_NOT = -1;
	
	private static MemberDao instance = new MemberDao();
	
	private MemberDao() {}
	
	public static MemberDao getInstance() {
		return instance;
	}
	
	public int insertMember(MemberDto dto) {
		
		int ri = 0;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		String query = "insert into members values(?,?,?,?,?,?)";
		
		try {
			connection = getConnection(); // connection으로 getConnection 메소드를 실행
			pstmt = connection.prepareStatement(query); // pstmt에 connection의 prepareStatement 메소드를 query인자를 주어 호출\
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPw());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.geteMail());
			pstmt.setTimestamp(5, dto.getrDate());
			pstmt.setString(6, dto.getAddress());
			pstmt.executeUpdate();
			ri = MemberDao.MEMBER_JOIN_SUCCESS;			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(connection !=null) connection.close();
			}	catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return ri;
	}
	

	public int confirmid(String id) {
		int ri = 0;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet set = null;
		String query = "select id from members where id = ?";
		
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, id);
			set = pstmt.executeQuery();
			if(set.next()) {
				ri = MemberDao.MEMBER_EXISTENT;
			} else {
				ri = MemberDao.MEMBER_NONEXISTENT;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(connection != null) connection.close();
				if(pstmt != null) pstmt.close();
				if(set != null) set.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ri;
	}
	public int userCheck( String id, String pw ) {
		int ri = 0;
		String dbPw;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet set = null;
		String query = "select pw from members where id = ?"; // members에서 id에 맞는 pw를 호출한다.
		
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(query); // pstmt는 connection을 통해 prepareStatement 메소드로 query 질의값에 대한 내용을 가져온다.
			pstmt.setString(1, id); //pstmt에 id값을 지정한다.
			set = pstmt.executeQuery(); // set으로 query문을 실행한다.
			
			if(set.next()) {
				dbPw = set.getString("pw");
				if(dbPw.equals(pw)) {
					ri = MemberDao.MEMBER_LOGIN_SUCCESS; // 로그인 OK
				} else {
					ri = MemberDao.MEMBER_LOGIN_PW_NO_GOOD; // 비번 X
				}
			} else {
				ri = MemberDao.MEMBER_LOGIN_IS_NOT; // 회원 X
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
			connection.close();
			pstmt.close();
			set.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ri;
	}
	public MemberDto getMember(String id) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet set = null;
		String query = "select * from members where id = ?";
		MemberDto dto = null;
		
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1,id);
			set = pstmt.executeQuery();
			
			if(set.next()) {
				dto = new MemberDto();
				dto.setId(set.getString("id"));
				dto.setPw(set.getString("pw"));
				dto.setName(set.getString("name"));
				dto.seteMail(set.getString("eMail"));
				dto.setrDate(set.getTimestamp("rDate"));
				dto.setAddress(set.getString("address"));
				
			}
		}catch (Exception e) {
				e.printStackTrace();
		} finally {
			try {
				set.close();
				connection.close();
				pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	} 
	
	public int updateMember(MemberDto dto) {
		int ri = 0;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		String query = "update members set pw=?, eMail=?, address=? where id=?";
		
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, dto.getPw());
			pstmt.setString(2, dto.geteMail());
			pstmt.setString(3, dto.getAddress());
			pstmt.setString(4, dto.getId());
			ri = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return ri;
	}
	private Connection getConnection() {
		
		Context context = null;
		DataSource dataSource = null;
		Connection connection = null;
		
		try {
				context = new InitialContext();
				dataSource = (DataSource)context.lookup("java:comp/env/jdbc/oracle11g");
				connection = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}	
}
