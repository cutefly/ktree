SELECT E.EMPLOYE_ID AS employeId, 
       E.NAME AS employeName, 
       E.PWD AS password, 
       E.USE_YN as useYn, 
       E.TEAM_CODE AS teamCode, 
       T.TEAM_NAME AS teamName, 
       E.DIVISION_CODE AS divisionCode,
	   D.DIVISION_NAME AS divisionName, 
	   A.AUTHORITY_LEVEL AS authLevel, 
	   E.POSITION AS position 
  from KPC_EMPLOYE E, KPC_DIVISION_INFO D, KPC_TEAM_INFO T, KPC_PERSONAL_AUTHORITY A 
 where E.EMPLOYE_ID = A.EMPLOYE_ID(+)
   AND E.TEAM_CODE = T.TEAM_CODE(+)
   AND E.DIVISION_CODE = D.DIVISION_CODE(+)
   AND E.USE_YN = 'Y'
;

SELECT E.EMPLOYE_ID AS employeId, 
       E.NAME AS employeName, 
       E.PWD AS password, 
       E.USE_YN as useYn, 
       E.TEAM_CODE AS teamCode, 
       T.TEAM_NAME AS teamName, 
       E.DIVISION_CODE AS divisionCode,
	   D.DIVISION_NAME AS divisionName, 
	   A.AUTHORITY_LEVEL AS authLevel, 
	   E.POSITION AS position 
  from KPC_EMPLOYE E 
  left OUTER join KPC_DIVISION_INFO D
    on E.DIVISION_CODE = D.DIVISION_CODE
  left OUTER join KPC_TEAM_INFO T
    on E.TEAM_CODE = T.TEAM_CODE
  left OUTER join KPC_PERSONAL_AUTHORITY A
    on E.EMPLOYE_ID = A.EMPLOYE_ID	
 where E.USE_YN = 'Y'
;
