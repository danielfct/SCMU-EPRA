<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

// include database and object files
include_once './config/database.php';
include_once './objects/history.php';

// instantiate database and product object
$database = new Database();
$db = $database->getConnection();

// initialize object
$history = new History($db);

// query history
$stmt = $history->read();
$num = $stmt->rowCount();

// check if more than 0 record found
if($num>0){

    // products array
    $history_arr=array();

    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);

        $history_item=array(
            "id" => $id,
            "evento" => $evento,
            "datahora" => $datahora,
        );

        array_push($history_arr, $history_item);
    }

    echo json_encode($history_arr);
}

else{
    http_response_code(404);
    echo json_encode(
        array("message" => "No history found.")
    );
}
?>
