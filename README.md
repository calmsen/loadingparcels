## Консольные посылки

### Команда load:
На вход идет список с названиями посылок. На выходе список с загруженными машинами.

load {название_входного_файла_с_посылкамию.csv}
#### Параметры команды:
- [--loading-mode { oneparcel | simple | uniform | efficient }] - Необязательный параметр. По умолчанию oneparcel
- [--trucks {размеры грузовиков}] - Необязательный параметр. Например значение 6x6\n5x6\n3x3 указывает, что необходимо загрузить в 3 грузовика с соответствующими кузовами  6x6, 5x6, 3x3  
- [--trucks-count {число_машин_для_погрузки}] - Необязательный параметр. По умолчанию 50. Игнорируется если --trucks пустой
- [--trucks-width {ширина_кузова}] - Необязательный параметр. По умолчанию 6. Игнорируется если --trucks пустой
- [--trucks-height {высота_кузова}] - Необязательный параметр. По умолчанию 6. Игнорируется если --trucks пустой
- [--out-format { txt | json | csv }] - Необязательный параметр. По умолчанию txt. Игнорируется если out-file имеет расширение txt, json или csv
- [--out-file {название_файла_для_записи_на_диск}] - Необязательный параметр. По умолчанию trucks.txt


Для выполнения команды погрузки, введите команду load {название_входного_файла_с_посылками.csv} и нажмите enter.  
По умолчанию будет команда load input.csv.

#### Доступные посылки по умолчанию:
1  

22  

333  

4444  

55555  

666  
666  

777  
7777  

8888  
8888  

999  
999  
999  


#### Пример файла c входными данными
Посылка тип 9
Посылка тип 6
Посылка тип 3
Посылка тип 8
Посылка тип 1
Посылка тип 1
Посылка тип 7
Посылка тип 2
Посылка тип 2


### Команда unload:
На вход идет json файл с загруженными машинами. На выходе список с посылками. 
unload {файл_в_формате_json.json}
#### Параметры команды:
- [--out-file {название_файла_для_записи_на_диск}] - Необязательный параметр. 
- [--out-format { txt | json | csv }] - Необязательный параметр. По умолчанию txt. Игнорируется если out-file имеет расширение txt, json или csv 
- [--with-count { false | true }] - Необязательный параметр. По умолчанию false. Разгрузка с подсчетом, если значение равно true.  


Для выполнения команды разгрузки, введите команду unload {файл_в_формате_json.json} или просто unload и нажмите enter.
По умолчанию будет команда unload trucks.json.

### Команда create:
На вход идут атрибуты посылки. Результатом является добавление посылки в репозиторий посылок.
create --name {название посылки} --form {форма посылки} --symbol {символ для отображения посылки}
#### Параметры команды:
- --name {название посылки} - Обязательный параметр. Например: Квадратное колесо
- --form {форма посылки} - Обязательный параметр. Например: xxx\nx x\nxxx
- --symbol {символ для отображения посылки} - Обязательный параметр. Например: o


Пример корректной команды: create --name Квадратное колесо --form xxx\nx x\nxxx --symbol o


### Команда update:
На вход идут атрибуты посылки. Результатом является редактирование посылки в репозитории посылок.
update --name {название посылки} --form {форма посылки} --symbol {символ для отображения посылки}
#### Параметры команды:
- --name {название посылки} - Обязательный параметр. Например: Квадратное колесо
- --form {форма посылки} - Обязательный параметр. Например: xxx\nx x\nxxx
- --symbol {символ для отображения посылки} - Обязательный параметр. Например: o


Пример корректной команды: update --name Квадратное колесо --form xxx\nx x\nxxx --symbol o


### Команда delete:
На вход название посылки. Результатом является удаление посылки из репозитория посылок.
delete {название посылки}
#### Параметры команды:
- отсутствуют


Пример корректной команды: delete Квадратное колесо


### Команда find:
На вход идут атрибуты посылки. Результатом является удаление посылки из репозитория посылок.
find [{название посылки}] - если не указывать название посылки, то будут выбраны все посылки
#### Параметры команды:
- [--out-format { txt | json | csv }] - Необязательный параметр. По умолчанию txt. Игнорируется если out-file имеет расширение txt, json или csv


Пример корректной команды: find Квадратное колесо.

