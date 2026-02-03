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

SELECT PS.SEQ AS seq, 
       E.EMPLOYE_ID AS employeId, 
       E.NAME AS employeName , 
       D.DIVISION_NAME AS divisionName, 
       T.TEAM_NAME AS teamName,
		   PS.SCORE1 AS score1, 
       PS.SCORE2 AS score2, 
       PS.SCORE3 AS score3, 
       PS.SCORE4 AS score4,
		   PS.SCORE5 AS score5, 
       PS.SCORE6 AS score6, 
       PS.SCORE7 AS score7, 
       PS.STATUS AS status 
  FROM KPC_EMPLOYE E
  left outer join KPC_DIVISION_INFO D
    on E.DIVISION_CODE = D.DIVISION_CODE
  left outer join  KPC_TEAM_INFO T
    on E.TEAM_CODE = T.TEAM_CODE
  left outer join KPC_PERSONAL_PROJECT_SCORE_HST PS
    on E.EMPLOYE_ID = PS.EMPLOYE_ID AND PS.MONTH = #{month}
  left outer join KPC_PERSONAL_AUTHORITY A
    on E.EMPLOYE_ID = A.EMPLOYE_ID AND A.AUTHORITY_LEVEL < 4
 WHERE E.USE_YN='Y'

		SELECT COUNT(*) AS cnt
		FROM
			KPC_EMPLOYE E
    LEFT OUTER JOIN
			KPC_PERSONAL_PROJECT_SCORE PS
		ON E.EMPLOYE_ID = PS.EMPLOYE_ID AND PS.MONTH = #{month}
		WHERE
			E.EMPLOYE_ID = #{employeId}
