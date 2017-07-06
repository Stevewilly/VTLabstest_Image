<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 $conn = mysqli_connect("localhost", "id716478_root", "steve", "id716478_stampload");
 $image = $_POST['image'];
 
 
 
 $sql = "INSERT INTO Images (image) VALUES (?)";
 
 $stmt = mysqli_prepare($conn,$sql);
 
 mysqli_stmt_bind_param($stmt,"s",$image);
 mysqli_stmt_execute($stmt);
 
 $check = mysqli_stmt_affected_rows($stmt);
 
 if($check == 1){
 echo "Image Uploaded Successfully";
 }else{
 echo "Error Uploading Image";
 }
 mysqli_close($conn);
 }else{
 echo "Error";
 }
?>