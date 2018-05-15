<?php
class User {

    // database connection and table name
    private $conn;
    private $table_name;

    // object properties
    public $nome;
    public $telemovel;
    public $email;
    public $password;
    public $admin;
    public $privilegios;

    // constructor with $db as database connection
    public function __construct($db) {
        $this->conn = $db;
        $this->table_name = "utilizador";
    }

    // read users
    function read() {
        // select all query
        $query = "SELECT * FROM " . $this->table_name;
        
        if ($this->email) {
            $query = $query . " WHERE email LIKE " . $this->email;
        }
      
        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // execute query
        $stmt->execute();
    }

    // create user
    function create() {

        // query to insert record
        $query = "INSERT INTO "
                  . $this->table_name . "
                  SET
                    nome=:nome, 
                    telemovel=:telemovel, 
                    email=:email, 
                    password=:password";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->telemovel=htmlspecialchars(strip_tags($this->telemovel));
        $this->email=htmlspecialchars(strip_tags($this->email));
        $this->password=htmlspecialchars(strip_tags($this->password));
        
        // bind values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":telemovel", $this->telemovel);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":password", $this->password);

        // execute query
        $stmt->execute();
    }
    
}
