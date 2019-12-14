<?php
require_once 'include/DB_Functions.php';
$db=new DB_Functions();
$response=array("error" =>FALSE);
$sid=$_POST['sid'];
$sem=$_POST['sem'];
$ia=$_POST['ia'];
$dept=$_POST['dept'];
$marks=$db->getmarks($sid,$ia);
$subject=$db->getsubjects($sem,$dept);
if($marks != false && $subject != false) {
if($sem==8)
{	if(!empty($marks))
	{
	$response["sid"] = $marks["sid"];
	$response["marks"]["ia"] = $marks["ia_no"];
	$response["marks"]["sub1"] = $marks["sub1"];
	$response["marks"]["sub2"] = $marks["sub2"];
	$response["marks"]["sub3"] = $marks["sub3"];
	$response["marks"]["sub4"] = $marks["sub4"];
	$response["marks"]["sub5"] = 0;
	$response["marks"]["sub6"] = 0;

	}	
	$response["subject"]["sem"] = $subject["sem"];
	$response["subject"]["sub1"] = $subject["sub1"];
	$response["subject"]["sub2"] = $subject["sub2"];
	$response["subject"]["sub3"] = $subject["sub3"];
	$response["subject"]["sub4"] = $subject["sub4"];
	$response["subject"]["sub5"] = "No Input";
	$response["subject"]["sub6"] = "No Input";
echo json_encode($response);
}
else
{		
if(!empty($marks))
	{
	$response["sid"] = $marks["sid"];
	$response["marks"]["ia"] = $marks["ia_no"];
	$response["marks"]["sub1"] = $marks["sub1"];
	$response["marks"]["sub2"] = $marks["sub2"];
	$response["marks"]["sub3"] = $marks["sub3"];
	$response["marks"]["sub4"] = $marks["sub4"];
	$response["marks"]["sub5"] = $marks["sub5"];
	$response["marks"]["sub6"] = $marks["sub6"];
	}	
	$response["subject"]["sem"] = $subject["sem"];
	$response["subject"]["sub1"] = $subject["sub1"];
	$response["subject"]["sub2"] = $subject["sub1"];
	$response["subject"]["sub3"] = $subject["sub1"];
	$response["subject"]["sub4"] = $subject["sub1"];
	$response["subject"]["sub5"] = $subject["sub5"];
	$response["subject"]["sub6"] = $subject["sub6"];
echo json_encode($response);
}
} else {
$response["error"]=TRUE;
echo json_encode($response);
}

?>


