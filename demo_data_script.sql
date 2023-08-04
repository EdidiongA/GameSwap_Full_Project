USE `cs6400-2022-01-Team028`;

# Download the postal_codes.csv file from https://gatech.instructure.com/courses/225464/modules/items/2278664
# Replace <path_to_postal_codes.csv> with the location of the downloaded csv file ie: /Users/josephtam/cs6400-2022-01-Team028/postal_codes.csv
SET GLOBAL local_infile = 1;
LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/postal_codes.csv'
INTO TABLE Location
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/users.csv'
INTO TABLE User
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/phone_numbers.csv'
INTO TABLE PhoneNumber
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

select * from PhoneNumber;

INSERT INTO Platform (platform_name)
VALUES
	('Nintendo'),
    ('PlayStation'),
    ('Xbox');

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/items.csv'
INTO TABLE Item
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/board_games.csv'
INTO TABLE BoardGame
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/card_games.csv'
INTO TABLE CardGame
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/video_games.csv'
INTO TABLE VideoGame
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/computer_games.csv'
INTO TABLE ComputerGame
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/jigsaw_puzzles.csv'
INTO TABLE JigsawPuzzle
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/swaps.csv'
INTO TABLE Swap
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/acknowledged_swaps.csv'
INTO TABLE AcknowledgedSwap
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/proposer_rated_counterparty_rated_swaps.csv'
INTO TABLE RatedSwap
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

LOAD DATA LOCAL INFILE '/Users/josephtam/cs6400-2022-01-Team028/counterparty_rated_proposer_rated_swaps.csv'
INTO TABLE RatedSwap
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;