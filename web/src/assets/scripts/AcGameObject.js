const AC_GAME_OBJECTS = [];

export class AcGameObject {
    constructor() {
        //将当前游戏对象加到数组里面去
        AC_GAME_OBJECTS.push(this);

        this.timedelta = 0;
        this.has_called_start = false;
    }

    start() {

    }

    update() {

    }

    on_destroy() {

    }
    //销毁当前对象
    destroy() {
        this.on_destroy();

        for (let i in AC_GAME_OBJECTS) {
            let obj = AC_GAME_OBJECTS[i];
            if (obj == this) {
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}

let last_timestamp = 0;

const step = (timestamp) => {
    //渲染并加载每个对象
    for (let obj of AC_GAME_OBJECTS) {
        if (!obj.has_called_start) {
            obj.has_called_start = true;
            obj.start();
        } else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step)
