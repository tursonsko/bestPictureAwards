If I would have more time:
1) I would completly finish logic in FillUpDatabaseService to make it more accurate (a'la google searcher) cause my solution has lacks when searching movie titles in Api with titles get from database. Not always the whole string is compatible, especially the other way round (Api -> Database)
2) I would deal with the problem of older films (which have the year of the Oscar event in the database, e.g. 1928/29). This creates problems in searching for API films where the production year is given as one annual date (e.g. 1928 or 1929). This requires that the mechanism is more intelligent and in the event of failure in searching after one year, it should try with the second available in the database.
3) I also noticed duplicate titles - by not solving the problem from point 2, this problem also occurs.
####Duplicated movies:
  - Cleopatra
  - Moulin Rouge
  - Heaven Can Wait
  - Mutiny on the Bounty
  - Romeo and Juliet

4) I would use Liquibase to deal with database (primo import .csv) and I would try to connect it with Docker, if it could be possible.

