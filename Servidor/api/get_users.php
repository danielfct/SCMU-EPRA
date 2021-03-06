<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// include database and object files
include_once './config/database.php';
include_once './objects/user.php';

// instantiate database and product object
$database = new Database();
$db = $database->getConnection();

// initialize object
$user = new User($db);

$filter = filter_input(INPUT_GET, 'search');
$stmt = $user->read($filter);
$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){

    // products array
    $user_arr=array();

    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);

        $user_item=array(
            "nome" => $nome,
            "telemovel" => $telemovel,
            "email" => $email,
            "password" => $password,
            "admin" => $admin,
            "privilegios" => $privilegios
        );

        array_push($user_arr, $user_item);
    }

    echo json_encode($user_arr);
}

else{
    echo json_encode(
        array("message" => "No users found.")
    );
}
?>
