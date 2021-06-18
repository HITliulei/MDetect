FROM 172.31.43.94:5000/lei_python:3
RUN mkdir -p /app
WORKDIR /app

#COPY requirements.txt ./
#RUN pip install cryptography
#RUN pip install --no-cache-dir -r requirements.txt
ADD givecommand.py /app/
ADD a.txt /app/
ADD b.txt /app/
CMD [ "python", "-u", "givecommand.py" ]
EXPOSE 32002