# Tips

## Linux user 홈디렉토리 변경
* 홈 디렉토리는 /home에 각 계정별로 생성됨.
* 계정마다 데이터를 계속 쌓다보면 결국 터짐! 펑!
* 기존 계정 홈디렉토리 변경
    * `usermod -d <new_home_dir> -m <user_name>`
* 앞으로 생성될 계정의 홈디렉토리 setting
    * `vi /etc/default/useradd`
    * `HOME=new_home_dir`
* Issue
    * 혹시 jupyterhub 사용중이였다면!
        * ipykernel을 사용하여 개인 conda 환경을 사용중이였더라면 기존 디렉토리를 계속 참조하고 있음.
        * 처리방법
            * 기존 ipykernel 삭제
                * `jupyter kernelspec uninstall 콘다환경이름`
            * 등록할 환경 activate
                * `conda activate 콘다환경이름`
            * ipykernel 재등록
                * `python -m ipykernel install --user --name "콘다환경이름" --display-name "화면상 이름"`
    * conda 사용중이였다면!
        * 단순히 cp로 옮기면 홈의 로컬에 있는 환경들의 세부 path들이 이상해짐!
        * 처리방법
            * 기존 환경의 복제본 생성
                * `conda create --name "new_환경" --clone "기존환경"`
            * 기존 환경 삭제
                * `conda env remove -n "기존환경"`
            * 주피터 허브 사용중이였으면 위의 ipykernel 삭제 추가 과정 반복!
            
            
## 서버 모니터링
* dstat
    * `dstat -t -p -c -m -d --disk-util -n`
* sar
    * `sar -interval 5`
* pidstat
    * 프로세스가 사용하는 리소스 사용률 
    * N초 동안 특정 리소스 사용한 모든 프로세스와 해당 프로세스가 사용한 리소스양 체크!
    * `pidstat -dl 5`