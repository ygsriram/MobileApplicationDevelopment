<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$Sid = $_POST["Sid"];
$rating = $_POST["Rating"];
$fid = $_POST["Fid"];
$subject = $_POST["subject"];
$comment1 = $_POST["comment1"];
$comment2 = $_POST["comment2"];
$recv = $db->apprisal_insert($Sid,$rating,$fid,$subject,$comment1,$comment2);
if($recv){
    $response["error"] = FALSE;
    $response["message"] = "Thank You";
    echo json_encode($response);
	}else{
$response["error"] = TRUE;
    $response["message"] = "Contact Admin";
}	
?>
<html>
    <head>
        <title>Google Cloud Messaging (GCM) Server in PHP</title>
    </head>
	<body>
	<h1>Register</h1> 
	<form action="rating.php/push=1" method="post"> 
	    Username:<br /> 
	    <input type="text" name="Rating" value="" /> 
<input type="text" name="Sid" value="" /> 
<input type="text" name="Fid" value="" /> 
<input type="text" name="subject" value="" /> 
<input type="text" name="comment1" value="" /> 
<input type="text" name="comment2" value="" /> 
	    <br /><br />
	    <input type="submit" value="Submit" /> 
		</form>
    </body>
</html>
