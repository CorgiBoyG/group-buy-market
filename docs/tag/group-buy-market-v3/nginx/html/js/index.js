function getCookie(name) {
  let cookieArr = document.cookie.split(";");
  for(let i = 0; i < cookieArr.length; i++) {
      let cookiePair = cookieArr[i].split("=");
      if(name == cookiePair[0].trim()) {
          return decodeURIComponent(cookiePair[1]);
      }
  }
  return null;
}

function generateRandomNumber(length) {
    let result = '';
    for (let i = 0; i < length; i++) {
        result += Math.floor(Math.random() * 10); // 生成 0-9 之间的随机数
    }
    return result;
}

// 轮播图逻辑
const swiperWrapper = document.querySelector('.swiper-wrapper');
const pagination = document.querySelector('.swiper-pagination');
let currentIndex = 0;

// 创建分页点
for(let i=0; i<3; i++) {
    const dot = document.createElement('div');
    dot.className = `swiper-dot${i===0 ? ' active' : ''}`;
    pagination.appendChild(dot);
}

// 自动轮播
setInterval(() => {
    currentIndex = (currentIndex + 1) % 3;
    swiperWrapper.style.transform = `translateX(-${currentIndex * 100}%)`;
    document.querySelectorAll('.swiper-dot').forEach((dot, index) => {
        dot.className = `swiper-dot${index === currentIndex ? ' active' : ''}`;
    });
}, 3000);

// 倒计时逻辑（完整实现）
class Countdown {
    constructor(element, initialTime) {
        this.element = element;
        this.remaining = this.parseTime(initialTime);
        this.timer = null;
        this.start();
    }

    parseTime(timeString) {
        const [hours, minutes, seconds] = timeString.split(':').map(Number);
        return hours * 3600 + minutes * 60 + seconds;
    }

    formatTime(seconds) {
        const h = Math.floor(seconds / 3600).toString().padStart(2, '0');
        const m = Math.floor((seconds % 3600) / 60).toString().padStart(2, '0');
        const s = (seconds % 60).toString().padStart(2, '0');
        return `${h}:${m}:${s}`;
    }

    update() {
        this.remaining--;
        if (this.remaining <= 0) {
            this.element.textContent = '00:00:00';
            this.stop();
            return;
        }
        this.element.textContent = this.formatTime(this.remaining);
    }

    start() {
        this.timer = setInterval(() => this.update(), 1000);
    }

    stop() {
        clearInterval(this.timer);
    }
}