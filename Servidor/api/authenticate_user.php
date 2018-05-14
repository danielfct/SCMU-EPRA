<?php 
    // Creating a connection
    $con = mysqli_connect("localhost:3306","scmu","epra","epra_db");
    if (mysqli_connect_errno())
    {
       echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }
    
    // Getting inputs from url
    $email = filter_input(INPUT_GET, "email");
    $pwd = filter_input(INPUT_GET, "pwd");
    
    // Query the database
    $sql = "Select * from utilizador where email like \"".$email."\"";
    if (!$result = mysqli_query($con, $sql))
    {
        echo "Query failed: " . mysqli_error($con);
    }
    else {   
        if (mysqli_num_rows($result) == 0) {
            echo json_encode("Email not found");
        } else {
            $row = mysqli_fetch_assoc($result);
            if (strcmp($row["password"], $pwd) == 0) {
                echo "Authenticated";
            } else {
                echo "Incorrect password";
            }
        }
        mysqli_free_result($result);
    }
    mysqli_close($con);
  
 ?>