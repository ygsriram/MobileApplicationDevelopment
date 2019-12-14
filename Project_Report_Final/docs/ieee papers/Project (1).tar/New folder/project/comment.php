<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
$fid = $_POST["Fid"];
$subject = $_POST["subject"];
$appr = $db->getappraisal($fid,$subject);
$count = $db->getcommentcount($fid,$subject);
$recv = $db->getcomment($fid,$subject);
$hcot = $db->getheadcount1($fid,$subject);
$sub = $db->noofstars($fid,$subject);
$i=5;
while($i!=0){
$response["a$i"]=$sub[$i];
$i--;
}
$j=$count["count(*)"];
$response["noofstars"]=$appr["avg(Rating)"];
$response["hcot"] = $hcot["count(*)"];
$i=0;$k=0;$c=0;
if($j!=0){
    $response["error"] = FALSE;
	while($i!=$j){
	if(strcmp($recv[$k+1],"Topic understood")==0){
$k+=4;

}else{
	$response["sid$c"] = $recv[$k];
    $response["dropbox$c"] = $recv[$k+1];
	$response["comment$c"] = $recv[$k+2];
	$response["rating$c"] = $recv[$k+3];
$c++;
$k+=4;
}$i++;}
$response["count"]=$c;
    echo json_encode($response);
}
else{
$response["error"] = TRUE;
    $response["sid0"] = "No comments";
    $response["dropbox0"] = "hello";
	$response["comment0"] = "hello";
echo json_encode($response);
}	
?>

