<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$classno = $_POST["classno"];
$lat0 = $_POST["lat0"];
$lon0 = $_POST["lon0"];
$lat1 = $_POST["lat1"];
$lon1 = $_POST["lon1"];
$lat2 = $_POST["lat2"];
$lon2 = $_POST["lon2"];
$lat3 = $_POST["lat3"];
$lon3 = $_POST["lon3"];
$db->insertintoroom($classno,$lat0,$lon0,$lat1,$lon1,$lat2,$lon2,$lat3,$lon3);
$response["error"] = FALSE;
$response["error_msg"]="Entered data";
echo json_encode($response);
?>
