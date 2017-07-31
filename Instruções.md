1 - Preparando o banco de dados.
	Foi utilizado o banco postgrees na versão 9.4
	Não há necessidade de criação das tabelas, pois foi utilizado a tecnologia Hibernate/JPA que realiza as criações automaticas das tabelas.
	- Para realizar as configurações necessárias, abra o arquivo persistence.xml.
	- O Arquivo persistence.xml fica em <Pasta do projeto Java>\restapp\src\main\java\META-INF\persistence.xml
	  
	  Inserir o servidor do banco na propriedade: <property name="hibernate.connection.url" value=" jdbc:postgresql://localhost:5432/postgres"/>
      Inserir o usuário do banco na propriedade: <property name="hibernate.connection.username" value="postgres"/>
      Inserir a senha do banco na propriedade: <property name="hibernate.connection.password" value="postgres"/>

2 - Importando o projeto.
	-Com o Eclipse aberto, vá em "File"->"Import"->"Existing Maven Projects", escolha a pasta "restapp" 
	que está no mesmo diretorio desse arquivo de texto, dê next até importar.
	O servidor utilizado para este projeto foi o Apache Tomcat 9.0
	- Com o servidor web já instalado no eclipse siga os passos abaixo:
		-Clique com o botão direito sobre o servidor e em "Add/Remove", adcione o modulo "restapp", inicie o mesmo.

3 - AngularJS
	-Após a inicialização do Apache, ir no projeto AngularFrontend via comando e executar o npm start, para inicializar o servidor nodeJs.
	-Para acessar o FrontEnd vá em http://localhost:<porta do NodeJS>
	-Pronto.
