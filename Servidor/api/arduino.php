<?php
// include database and object file
include_once './config/database.php';

// get database connection
$database = new Database();
$conn = $database->getConnection();

$query = "SELECT estadoAtual FROM alarme LIMIT 1";

$stmt = $conn->prepare($query);

// execute query
$stmt->execute();

$row = $stmt->fetch(PDO::FETCH_ASSOC);
    // extract row
    // this will make $row['name'] to
    // just $name only
    $alarmState = $row["estadoAtual"] == 0 ? "off" : "on";
        echo "alarme=".$alarmState." ";


$query = "SELECT nome, ligado FROM devices";

$stmt = $conn->prepare($query);

// execute query
$stmt->execute();
$counter = 1;
while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {;
  $state = $row["ligado"] == 0 ? "off" : "on";
  echo ($counter <= 2 ? "led".$counter : $row["nome"])."=".$state." ";
  $counter++;
}
?>
