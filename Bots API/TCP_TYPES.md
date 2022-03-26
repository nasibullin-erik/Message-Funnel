/*---------------------------------------------------------------------------*\
# `INFORMATION ABOUT TCP TYPES`

## *0 - 9 : service types*

### 0 : re-query in case of fcs distortion

###### order - botMessage id

### 1-2 : registration frames

#### (1) management-client - contains bot token
###### order - botMessage id and bot token.

#### (2) connection-server - answer on current token and successfully connection
###### order - botMessage id, messengerType and botName

### 3 : connection-server - send new botMessage from user. Contains userId, userNickname, messageText
###### order - botMessage id, userId, userNickname and messageText

### 4 : management-client - reply on some botMessage. Contains userId, replyText
###### order - botMessage id, userId and replyText

/*---------------------------------------------------------------------------*\