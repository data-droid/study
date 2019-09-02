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