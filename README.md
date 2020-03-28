## Wiremock

Run

```
java -jar wiremock-standalone-2.24.1.jar
```


### Wiremock recorder

Run wiremock on some port

```
java -jar wiremock-standalone-2.24.1.jar --port 8081
```

Open recorder in the browser

```
http://localhost:8081/__admin/recorder/
```

F.ex. you want to record the following endpoint:

```
https://swapi.co/api/people
```

Put target URL: `https://swapi.co` and click record button. In a separate window just call your api endpoint using the proxy:

```
http://localhost:8081/api/people
```

Stop recording, you should see: `Captured 1 stub mappings.`. Go to your `{wiremock-bin}/mappings` and open .json file.