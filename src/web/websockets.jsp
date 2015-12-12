<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>websocket设置界面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="pageID" content="update-service"/>
  </head>
  
  <body>
    <div class="jive-table">
        <table cellpadding="0" cellspacing="0" border="0" width="40%">
                <tr>
                    <td align="center">websocket访问连接路径：</td>
                    <td align="center"><input type="text" id="webAppName" name="webAppName"/></td>
                    <td align="center"><input id="update-serverName" type="button" value="更新"></td>
                </tr>
        </table>
    </div>
  		<script src="http://quimg.com/admin/assets/js/base/jquery-2.0.3.min.js"></script>
  		<script type="text/javascript">
	  		$('#update-serverName').click(function(){
	  			var webAppName = $("#webAppName").val();
	  			$.ajax({
	  				url: "/plugins/websockets/server/update?webAppName="+webAppName,
	  		        success: function (data) {
	  		            var dt = jQuery.parseJSON(data);
	  		            var result = dt.result;
	  		            if(result==true)
	  		            	alert("修改成功，"+dt.message);
	  		            else
	  		            	alet("修改失敗，"+dt.message);
	  		        }
	  			});
	  		});  
  		</script>
  </body>
</html>