<?php
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }


 
    /**
     * Storing new user
     * returns user details
     */
	public function insertintoroom($classno,$lat0,$lon0,$lat1,$lon1,$lat2,$lon2,$lat3,$lon3){
$stmt = $this->conn->prepare("INSERT INTO room_cordinate(roomno, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3) VALUES (?,?,?,?,?,?,?,?,?)");
 $stmt->bind_param("sssssssss",$classno,$lat0,$lon0,$lat1,$lon1,$lat2,$lon2,$lat3,$lon3);
 $stmt->execute();
          $stmt->close();  
}
public function getroomdetails($classno){
$stmt = $this->conn->prepare("SELECT *  FROM room_cordinate WHERE roomno = ?");
 	$stmt->bind_param("s", $classno);
 	if ($stmt->execute()) {
            $loct = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $loct;
}
else return false;
}
   public function storeUser($sid,$password) {
        
        
 	$stmt = $this->conn->prepare("SELECT id from log_pwd WHERE id = ?");
         $stmt->bind_param("s", $sid);
         $stmt->execute();
         $stmt->store_result();
	if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
	$stmt = $this->conn->prepare("DELETE FROM log_pwd WHERE id= ?");
         $stmt->bind_param("s", $sid);
         $stmt->execute();
          $stmt->close();  
	}
        $stmt = $this->conn->prepare("INSERT INTO log_pwd(id, password) VALUES(?, ?)");
        $stmt->bind_param("ss", $sid,$password);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM student_details WHERE Sid = ?");
            $stmt->bind_param("s", $sid);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
	public function type_user($sid){
	$stmt = $this->conn->prepare("SELECT type FROM detail WHERE id = ?");
 	$stmt->bind_param("s", $sid);
 	$stmt->execute();
            $type = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $type;
}
    public function getstudentmailid($sid) {
 
        $stmt = $this->conn->prepare("SELECT *  FROM student_details WHERE Sid = ?");
 
        $stmt->bind_param("s", $sid);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
             return $user;
            
        }
    }
public function getfacultymailid($sid) {
 
        $stmt = $this->conn->prepare("SELECT *  FROM faculty_details WHERE Fid = ?");
 $stmt->bind_param("s", $sid);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
             return $user;
            
        }
    }
    
public function att_status($Sid,$sub_code)
{
$stmt = $this->conn->prepare("select * from feedback where Sid = ? and subject = ?");
$stmt->bind_param("ss",$Sid,$sub_code);
$stmt->execute(); 
$att = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $att;
}
public function fid_subname($subcode){
$stmt = $this->conn->prepare("select * from subject_details where subcode = ?");
$stmt->bind_param("s",$subcode);
$stmt->execute(); 
$att = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $att;
}
public function sub_count($sem)
{
$stmt = $this->conn->prepare("select count(*) from subject_details where sem = ?");
$stmt->bind_param("s",$sem);
$stmt->execute(); 
$att = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $att;
}
public function getcomment($fid,$subject){
$stmt = $this->conn->prepare("select Sid,dropdown,comment,Rating from feedback where Fid = ? and subject = ? and time = (SELECT time FROM feedback where Fid = ? and subject = ? ORDER BY time DESC LIMIT 1 )");
$stmt->bind_param("ssss",$fid,$subject,$fid,$subject);
$stmt->execute(); 
$stmt->bind_result($abc,$def,$ghi,$klm);
$att = array();
while($stmt->fetch()){
    $att[] = $abc;
$att[] = $def;
$att[] = $ghi;
$att[] = $klm;
}
$stmt->free_result();
$stmt->close();
return $att;
}
public function getcommentcount($fid,$subject){
$stmt = $this->conn->prepare("select count(*) from feedback where Fid = ? and subject = ? and time = (SELECT time FROM feedback where Fid = ? and subject = ? ORDER BY time DESC LIMIT 1 )");
$stmt->bind_param("ssss",$fid,$subject,$fid,$subject);
$stmt->execute(); 
$att = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $att;
}
public function sub_details($sem)
{
$stmt = $this->conn->prepare("select subject,subcode from subject_details where sem = ?");
$stmt->bind_param("s",$sem);
$stmt->execute(); 
$stmt->bind_result($abc,$def);
$att = array();
while($stmt->fetch()){
    $att[] = $abc;
$att[] = $def;
}
$stmt->free_result();
$stmt->close();
return $att;
}
public function noofstars($fid,$subject){
$i=5;
$att = array();
while($i!=0){
$stmt = $this->conn->prepare("select count(*) from feedback where Fid=? and subject=? and Rating>$i-1 and Rating<=$i and time = (SELECT time FROM feedback where Fid = ? and subject = ? ORDER BY time DESC LIMIT 1 )");
$stmt->bind_param("ssss",$fid,$subject,$fid,$subject);
$stmt->execute(); 
$stmt->bind_result($abc);
$stmt->fetch();
$att[$i] = $abc;
$stmt->free_result();
$stmt->close();
$i--;
}
return $att;
}
public function no_attendance($Sid,$sub_code)
{
$stmt = $this->conn->prepare("select count(*) from feedback where Sid = ? and subject = ?");
$stmt->bind_param("ss",$Sid,$sub_code);
$stmt->execute(); 
$att = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $att;
}
public function getappraisal($fid,$subcode){
$stmt = $this->conn->prepare("Select avg(Rating) from feedback where Fid = ? and time = (SELECT time FROM feedback where Fid = ? and subject = ? ORDER BY time DESC LIMIT 1 ) and subject = ? ");
$stmt->bind_param("ssss",$fid,$fid,$subcode,$subcode);
$stmt->execute(); 
$appr = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $appr;
}
public function lasttaken($fid,$subcode){
$stmt = $this->conn->prepare("SELECT time FROM feedback where Fid = ? and subject = ? ORDER BY time DESC LIMIT 1");
$stmt->bind_param("ss",$fid,$subcode);
$stmt->execute(); 
$appr = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $appr;
}
public function noofclasses($sub_code,$sec)
{
$stmt = $this->conn->prepare("select * from no_class_taken where subcode = ? and sec = ?");
$stmt->bind_param("ss",$sub_code,$sec);
$stmt->execute(); 
$att = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $att;
}
public function removefeedback($fid,$subcode){
$stmt = $this->conn->prepare("DELETE from feedback where Fid = ? and subject = ? and time = CURDATE()");
 	$stmt->bind_param("ss", $fid,$subcode);
 	$stmt->execute();
	$stmt->close();
}
public function getheadcount1($fid,$subcode){
$stmt = $this->conn->prepare("SELECT count(*) from feedback where Fid = ? and subject = ? and time = (SELECT time FROM feedback where Fid = ? and subject = ? ORDER BY time DESC LIMIT 1 )");
 	$stmt->bind_param("ssss", $fid,$subcode,$fid,$subcode);
 	if ($stmt->execute()) {
            $loct = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $loct;
}
}
public function getheadcount($fid,$subcode){
$stmt = $this->conn->prepare("SELECT count(*) from feedback where Fid = ? and subject = ? and time = CURDATE()");
 	$stmt->bind_param("ss", $fid,$subcode);
 	if ($stmt->execute()) {
            $loct = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $loct;
}
else return false;
}
public function inc_noct($subcode,$sec)
{
$stmt = $this->conn->prepare("select * from no_class_taken where subcode = ? and sec = ?");
$stmt->bind_param("ss",$subcode,$sec);
$stmt->execute();
$stmt->store_result();
if($stmt->num_rows>0)
{
$stmt->close();
$stmt = $this->conn->prepare("update no_class_taken set noct = noct+1, date = CURDATE() where subcode = ? and sec = ?");
$stmt->bind_param("ss",$subcode,$sec);
$stmt->execute();
$stmt->close();
}
else {
$stmt->close();
$stmt = $this->conn->prepare("INSERT INTO no_class_taken(subcode, fid, noct, sec,date) VALUES (?,NULL,1,?,CURDATE())");
$stmt->bind_param("ss",$subcode,$sec);
$stmt->execute();
$stmt->close();
}
}
	public function apprisal_insert($Sid,$rating,$Fid,$subject,$comment1,$comment2)
{
$stmt = $this->conn->prepare("INSERT INTO feedback(Sid,Rating,time,Fid,subject,dropdown,comment) values(?,?,CURDATE(),?,?,?,?)");
$stmt->bind_param("ssssss", $Sid,$rating,$Fid,$subject,$comment1,$comment2);
$stmt->execute();
$stmt->close();
$stmt = $this->conn->prepare("select * from feedback where Sid = ? and subject = ? and time = CURDATE()");
$stmt->bind_param("ss", $Sid,$subject);
$stmt->execute();
 $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
return $user;
}
   public function isUserExisted($sid) {
        $stmt = $this->conn->prepare("SELECT id from detail WHERE id = ?");
         $stmt->bind_param("s", $sid);
         $stmt->execute();
         $stmt->store_result();
         if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
	public function login_status($sid)
{
$stmt = $this->conn->prepare("SELECT * from login WHERE id = ?");
         $stmt->bind_param("s", $sid);
         $stmt->execute();
         if ( $stmt->execute()) {
            // user existed
	    $user = $stmt->get_result()->fetch_assoc();
	    $stmt->close();
	    return $user;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
}

public function validate_password($sid,$password,$deviceid,$type)
{
$stmt = $this->conn->prepare("SELECT * from log_pwd where id = ?");
$stmt->bind_param("s",$sid);
if($stmt->execute()) {
$pws = $stmt->get_result()->fetch_assoc();
$stmt->close();
$enc_pwd = $pws['password'];

if(strcmp($enc_pwd,$password)==0)
{
$stmt = $this->conn->prepare("INSERT INTO `login`(`id`, `deviceid`, `logged_at`) VALUES (?,?,NOW())");
        $stmt->bind_param("ss", $sid,$deviceid);
$result = $stmt->execute();
//echo $result;
        if(!$result)
        $stmt->close();
if(strcmp($type,"student")==0){
$stmt = $this->conn->prepare("SELECT *  FROM student_details WHERE Sid = ?");
 	$stmt->bind_param("s", $sid);
$result = $stmt->execute();
//echo $result;
 	if ($result) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $user;
}
}else {
$stmt = $this->conn->prepare("SELECT *  FROM faculty_details WHERE Fid = ?");
 	$stmt->bind_param("s", $sid);
 	$result = $stmt->execute();
//echo $result;
 	if ($result)  {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $user;
}
} 
}
}
}
public function fac_count($fid){
$stmt = $this->conn->prepare("select count(*) from sub_handled where fid = ?");
$stmt->bind_param("s",$fid);
$stmt->execute(); 
$att = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $att;
}
public function sub_handled($fid) {
$stmt = $this->conn->prepare("select s.subcode,subject from sub_handled s,subject_details d where s.fid=? and d.subcode = s.subcode ");
$stmt->bind_param("s",$fid);
$stmt->execute(); 
$stmt->bind_result($abc,$def);
$att = array();
while($stmt->fetch()){
    $att[] = $abc;
$att[] = $def;
}
$stmt->free_result();
$stmt->close();
return $att;
}
public function userdetails($sid)
{
$stmt = $this->conn->prepare("SELECT *  FROM student_details WHERE Sid = ?");
 	$stmt->bind_param("s", $sid);
 	if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 	return $user;
}
}
public function delete_entry($sid)
{
$stmt = $this->conn->prepare("DELETE FROM login WHERE sid = ?");
$stmt->bind_param("s",$sid);
$stmt->execute();
$stmt->close();
}
public function getmarks($sid,$ia) {
$stmt = $this->conn->prepare("SELECT * from ia_asmt where sid = ? and ia_no = ?");
$stmt->bind_param("ss",$sid,$ia);
if($stmt->execute())
{
            $marks = $stmt->get_result()->fetch_assoc();
	    $stmt->close();
return $marks;
} else {
return NULL;}
}
public function getsubjects($sem,$dept) {
$stmt = $this->conn->prepare("SELECT * from subject where sem = ? and dept = ?");
$stmt->bind_param("ss",$sem,$dept);
$stmt->execute();
$subjects = $stmt->get_result()->fetch_assoc();
            $stmt->close();
return $subjects;

}
public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
}
?>
