# docker run -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
docker start 80742f1518ea # local postgres image id
docker update --restart unless-stopped 80742f1518ea # update restart policy
