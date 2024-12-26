## Консольные посылки

### Команда load:
На вход идет файл с посылками разного размера и формы.

load [название_входного_файла_с_посылками.txt] [--mode { onebox | simple | uniform | efficient }] [--count число_машин_для_погрузки] [--format { txt | json }] [--out название_файла_для_записи_на_диск]

Для выполнения команды погрузки, введите команду load {название_входного_файла_с_посылками.txt} или просто нажмите enter.  
По умолчанию будет команда load input.txt --mode onebox --count 10 --format txt

### Команда unload:
На вход идет json файл с загруженными машинами.
unload [файл_в_формате_json.json] [--out [название_файла_для_записи_на_диск]]

Для выполнения команды разгрузки, введите команду unload {файл_в_формате_json.json} или просто unload и нажмите enter.  
По умолчанию будет команда unload trucks.json --out parcels.txt

### Возможные посылки:
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


### Пример файла c входными данными
999
999
999

666
666

55555

1

1

333
