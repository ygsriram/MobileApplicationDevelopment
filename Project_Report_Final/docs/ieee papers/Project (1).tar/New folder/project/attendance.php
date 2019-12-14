<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$sid = $_POST["Sid"];
$subcode = $_POST["subcode"];
$sec = $_POST["sec"];
$user = $db->att_status($sid, $subcode);
$att = $db->no_attendance($sid,$subcode);
$abc = $db->noofclasses($subcode,$sec);
if($abc["noct"]==null){
$response["error"] = FALSE;
$response["no_att"]=$att["count(*)"];
$response["noct"]=0;
$response["subcode"] = $subcode;
$response["time"] = date("Y-m-d");;
echo json_encode($response);
}
else {$response["error"] = FALSE;
$response["no_att"]=$att["count(*)"];
$response["noct"]=$abc["noct"];
$response["subcode"] = $subcode;
$response["time"] = $abc["date"];
echo json_encode($response);
}
?>
 
