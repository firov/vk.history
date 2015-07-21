VK History
========

Simple message history exporter for vkontakte ( https://vk.com )

- [Homepage](https://github.com/firov/vk.history)
- [MIT License](LICENSE)

### Overview

Exports your history to text or html format. Currently exports only text without images.

### Usage

You need java 8 (jre-8) to run this application and access token from vk.com. 
Visit https://oauth.vk.com/authorize?client_id=4917578&scope=messages&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.32&response_type=token press allow button and then copy token from url
https://oauth.vk.com/blank.html#access_token=__TOKEN__&expires_in=86400 where is __TOKEN__ is the text you need.

Then run application:
```
java -jar vk.history.jar -t __TOKEN__
```

Options:
```
-f txt|html -- exports in txt or html
-wp -- exclude photos
-wc -- exclude chats
-wd -- exclude dialogs
```