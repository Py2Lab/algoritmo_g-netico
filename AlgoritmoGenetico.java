/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
/**
 *
 * @author gibranaguilar
 */

public class AlgoritmoGenetico {

  int Matriz[][];

  /**
   * @param args the command line arguments
   */

  public AlgoritmoGenetico(int Matriz[][]) {
    this.Matriz = Matriz;
  }

  public static int [][] CrearPoblacion(int Matriz[][]){

    int numerodeciudades=Matriz.length;//Variable que nos sirve para saber el numero de ciudades.
    int tamanoPoblacion=(int)(numerodeciudades/5);//Aqui defino el tamano de la poblacion, el
    //autor maneja 20 por ciento por ello pongo lo mismo.
    int poblacion[][]=new int[tamanoPoblacion][numerodeciudades];//Lista de poblacion.
    int elementoPoblacion[]=new int [numerodeciudades];//Este arreglo nos sirve para guardar un
    //tour aleatorio y luego agregarlo a la lista.
    boolean ArregloAuxiliar[]=new boolean[numerodeciudades];//Arreglo que nos sirve para no repetir
    //elementos en la permutacion.
    int permutacionesAleatorias=0;

    //Este ciclo es de permutaciones .
    while(permutacionesAleatorias<(int)(tamanoPoblacion/2)){

      //Este while nos sirve para asegurar que tenemos una poblacion del tamano deseado
      int elementoCamino=0;
      while(elementoCamino<numerodeciudades){
        //Este while nos sirve para asegurar que nuestras permutaciones son del tamano del numero de
        //ciudades.
        int numero= (int)(Math.random() * numerodeciudades);
        //Creamos un random y vemos que aun no lo agregamos al tour en cuyo caso lo agregamos y en caso
        //contrario lanzamos otro random.
        if(!ArregloAuxiliar[numero]){
          elementoPoblacion[elementoCamino]=numero;
          elementoCamino=elementoCamino+1;
          ArregloAuxiliar[numero]=true;
          }
        }
        int pesoTour=0;
        for(int i=0;i<elementoPoblacion.length;i++){
          pesoTour = Matriz[elementoPoblacion[i%elementoPoblacion.length]]
                    [elementoPoblacion[(i+1)%elementoPoblacion.length]] + pesoTour;
        }
        //Esta condicion depende de la red que utilices, en el caso de la red que nos dieron es el resultado .
        if(pesoTour<28000){
        //Aqui imprimimos lo tour que vamos creando.
        //Nota deberiamos verificar que las aristas del tour existen en la matriz pero no es necesario por que
        //tenemos una grafica completa.

        for(int i=0;i<numerodeciudades;i++){
          System.out.print(elementoPoblacion[i]);
          System.out.print(",");
          poblacion[permutacionesAleatorias][i]=elementoPoblacion[i];
          ArregloAuxiliar[i]=false;//Regresas al estado original la matriz para crear mas tours.
        }
        System.out.println("");
        permutacionesAleatorias+=1;
        }else{
          //Aqui solo regresamos a ArregloAuxiliar a false para generar otra permutacion que cumpla que su peso sea menor
          //al valor propuesto
          for(int i=0;i<numerodeciudades;i++){
            ArregloAuxiliar[i]=false;//Regresas al estado original la matriz para crear mas tours.
          }
        }
      }//Al final de este ciclo las permutaciones que agregamos son muy buenas, y por debajo del promedio de pesos de tour.
      //Este ciclo es de permutaciones completamente aleatorio.
      while(permutacionesAleatorias<tamanoPoblacion){
        //Este while nos sirve para asegurar que tenemos una poblacion del tamano deseado
        int elementoCamino=0;
        while(elementoCamino<numerodeciudades){
          //Este while nos sirve para asegurar que nuestras permutaciones son del tamano del numero de
          //ciudades.
          int numero= (int)(Math.random() * numerodeciudades);
          //Creamos un random y vemos que aun no lo agregamos al tour en cuyo caso lo agregamos y en caso
          //contrario lanzamos otro random.
          if(!ArregloAuxiliar[numero]){
              elementoPoblacion[elementoCamino]=numero;
              elementoCamino=elementoCamino+1;
              ArregloAuxiliar[numero]=true;
          }
        }
        //Aqui imprimimos lo tour que vamos creando.
        //Nota deberiamos verificar que las aristas del tour existen en la matriz pero no es necesario por que
        //tenemos una grafica completa.
        for(int i=0;i<numerodeciudades;i++){
          System.out.print(elementoPoblacion[i]);
          System.out.print(",");
          poblacion[permutacionesAleatorias][i]=elementoPoblacion[i];
          ArregloAuxiliar[i]=false;//Regresas al estado original la matriz para crear mas tours.
        }
        System.out.println("");
        permutacionesAleatorias+=1;
      }
      //Cuando la poblacion tiene el tamano que deseamos regresamos la poblacion.
      return poblacion;
    }

    /**
    *
    * @param tour el tour al que le quieres aplicar la funcion de evaluacion.
    * @param Matriz La matriz a la que pertenece el tour.
    * @return int La suma de los pesos de las aristas del tour.
    */

    public static int fitness(int tour[], AlgoritmoGenetico Matriz){

      int pesoTour=0;
      int[][] MatrizPesos = Matriz.Matriz;
      int renglones=MatrizPesos.length;
      int columnas=MatrizPesos[0].length;
      /**
      * En este for recorremos las aristes del tour y las sumamos para optener la
      * funcion de evaluacion.
      */
      for(int i=0;i<tour.length;i++){
        pesoTour = MatrizPesos[tour[i%tour.length]%renglones]
                  [tour[(i+1)%tour.length]%columnas] + pesoTour;
      }
        return pesoTour;
    }

    public static int[] PartiallyMappedCrossover(int father[],int mother[]){
      /**
      * Copias el intervalo aleatorio elegido, luego ves que elementos no estan en el intervalo
      * y ves el elemento correspondiente en father que no aparece en mother y lo cambias por la posicion
      * en mother igual al elemento en father, si ya estaba en la subcadena repites el proceso hasta que no este y
      * tengas n genes.
      */

      int numerodeCiudades = father.length;//Variable nos ayuda a saber el numero de ciudades.
      //Este arreglo nos dice quienes fueron visitados en el intervalo al azar elegido.
      boolean VisitadosFather[]=new boolean[numerodeCiudades];
      int hijo[]=new int[numerodeCiudades];//Arreglo que representa al hijo.
      for(int l=0;l<hijo.length;l++){
          hijo[l]=-1;
      }
      boolean auxiliar=false;
      int numeroAuxiliar1 = (int)(Math.random() * numerodeCiudades);//Valor al azar para elegir el intervalo
      int numeroAuxiliar2 = (int)(Math.random() * numerodeCiudades);//Valor al azar para elegir el intervalo
      System.out.println(numeroAuxiliar1);
      System.out.println(numeroAuxiliar2);
      if(numeroAuxiliar1<numeroAuxiliar2){
        for(int i=numeroAuxiliar1;i<=numeroAuxiliar2;i++){
            hijo[i]=father[i];
            //Luego marcamos con true para saber que fue visitado.
            VisitadosFather[father[i]]=true;
        }
        for(int i=numeroAuxiliar1;i<numeroAuxiliar2;i++){
          for(int j=numeroAuxiliar1;j<numeroAuxiliar2;j++){
            if(mother[i]==father[j]){
                auxiliar=true;
            }
          }
        if(!auxiliar){
          for(int j=0;j<numerodeCiudades;j++){
            //En este for buscamos la posicion donde el gen de la madre es igual al gen del padre
            //y la cambiamos a true para despues agregar ese gen al hijo.
            if(father[i]==mother[j]){
              hijo[j]=mother[i];
              VisitadosFather[j]=true;
              break;//al encontrarlo salimos del ciclo.
            }
          }
        }
      }
      for(int i=0;i<numerodeCiudades;i++){
        if(!VisitadosFather[i] && hijo[i]==-1){
                  hijo[i]=mother[i];
                  VisitadosFather[i]=true;
              }
           }
      for(int i=0;i<numerodeCiudades;i++){
        for(int j=0;j<numerodeCiudades;j++){
          if(hijo[i]==-1 && !VisitadosFather[j]){
            hijo[i]=j;
            VisitadosFather[j]=true;
            break;
          }
        }
      }
      }else{
        for(int i=numeroAuxiliar2;i<=numeroAuxiliar1;i++){
          hijo[i]=father[i];
          //Luego marcamos con true para saber que fue visitado.
          VisitadosFather[father[i]]=true;
        }
        for(int i=numeroAuxiliar2;i<numeroAuxiliar1;i++){
          for(int j=numeroAuxiliar2;j<numeroAuxiliar1;j++){
            if(mother[i]==father[j]){
              auxiliar=true;
          }
        }
        if(!auxiliar){
            for(int j=0;j<numerodeCiudades;j++){
              //En este for buscamos la posicion donde el gen de la madre es igual al gen del padre
              //y la cambiamos a true para despues agregar ese gen al hijo.
              if(father[i]==mother[j]){
                  hijo[j]=mother[i];
                  VisitadosFather[j]=true;
                  break;//al encontrarlo salimos del ciclo.
              }
            }
        }
      }
      for(int i=0;i<numerodeCiudades;i++){
        if(!VisitadosFather[i] && hijo[i]==-1){
          hijo[i]=mother[i];
        }

      }

      for(int i=0;i<numerodeCiudades;i++){
        for(int j=0;j<numerodeCiudades;j++){
            if(hijo[i]==-1 && !VisitadosFather[j]){
                hijo[i]=j;
                VisitadosFather[j]=true;
                break;
            }
        }
      }
    }
    return hijo;
    }


    public static int[] OrderCrossover(int father[],int mother[]){
      /**
       * Copias el intervalo aleatorio, luego desde el punto final del intervalo vas agregando
       * los elementos que no han sido agregados y regresas al inicio si aun no agregas todos.
       */

      int numerodeCiudades = father.length;// Variable para saber el numero de ciudades.
      //Este arreglo nos dice quienes fueron visitados en el intervalo al azar elegido.
      boolean VisitadosFather[]=new boolean[numerodeCiudades];
      //Creamos al hijo y lo inicializamos en -1 para no confundir ninguna posicion con la ciudad 0.
      int hijo[]=new int[numerodeCiudades];
      for(int l=0;l<hijo.length;l++){
        hijo[l]=-1;
      }
      //Aqui elegimos los dos numeros aleatorios.
      int numeroAuxiliar1 = (int)(Math.random() * numerodeCiudades);
      int numeroAuxiliar2 = (int)(Math.random() * numerodeCiudades);
      //imprimimos los numeros aleatorios para verificar que el algoritmo es correcto.
      //System.out.println(numeroAuxiliar1);
      //System.out.println(numeroAuxiliar2);
      //Este if es para saber quien es mayor y poder copiar el intervalo adeacuado al hijo.
      if(numeroAuxiliar1<numeroAuxiliar2){
        for(int i=numeroAuxiliar1;i<=numeroAuxiliar2;i++){
          //Aqui copiados el intervalo del padre al hijo.
          hijo[i]=father[i];
          //Luego marcamos con true para saber que fue visitado.
          VisitadosFather[father[i]]=true;
          }
          //En este for recoremos desde el fin del intervalo hasta el final y regresamos, con el modulo,
          //hasta agregar los genes de la madre en orden que no agrego el padre.
          for(int j=numeroAuxiliar2+1;j<=numeroAuxiliar2+numerodeCiudades;j++){
            for(int k = numeroAuxiliar2+1;k<=numeroAuxiliar2+numerodeCiudades;k++){
            //Verificamos que no ha sido visitado y que aun no agregamos en el hijo .
              if(!VisitadosFather[mother[k%numerodeCiudades]] && hijo[j%numerodeCiudades]==-1){
                //Agregamos el gen de la madre en la posicion correspondiente.
                hijo[j%numerodeCiudades] = mother[k%numerodeCiudades];
                VisitadosFather[ mother[k%numerodeCiudades]]=true;//marcamos como visitado.
              }
            }
          }
          }else{
            //En este el es analogo pero cuando numeroAuxiliar2<=numeroAuxiliar1.
            for(int i=numeroAuxiliar2;i<=numeroAuxiliar1;i++){
              hijo[i]=father[i];
              VisitadosFather[father[i]]=true;
              }
            for(int j=numeroAuxiliar1+1;j<=numeroAuxiliar1+numerodeCiudades;j++){
              for(int k=numeroAuxiliar1+1;k<=numeroAuxiliar1+numerodeCiudades;k++){
                if( !VisitadosFather[mother[k%numerodeCiudades]] && hijo[j%numerodeCiudades]==-1){
                  hijo[j%numerodeCiudades]= mother[k%numerodeCiudades];
                  VisitadosFather[mother[k%numerodeCiudades]]=true;
                  }
                }
            }
           }
      return hijo;
    }

    /**  Hay muchas variantes de esta mutacion, la que vimos en clase es:
    *
    * Ves la primera posicion del padre y la madre, luego guardas los valores y vas al lugar donde
    * esta el valor de la primera posicion de la madre en el padre y repites el proceso hasta que regreses al
    * primer valor del padre, es decir cierres el ciclo, despues pones los elementos del ciclo del padre en el hijo
    * y los que faltan los pones en orden desde la madre.
    * @param father
    * @param mother
    * @return int []
    */

    public static int[] CycleCrossover(int father[],int mother[]){

      int numerodeCiudades = father.length;//Variable para saber numero de ciudades.
      boolean CiudadesdelCiclo[]=new boolean[numerodeCiudades];// Este arreglo guardara la informacion
      // de las ciudades en el cciclo, true si estan el el ciclo y false si no lo estan.
      int hijo[]=new int[numerodeCiudades];

      /**
      *
      * La primera  del padre siempre esta.
      */
      CiudadesdelCiclo[0]=true;

      //Iniciamos la posicion actual del ciclo en mother[0, ya que ya hemos agregado al primer elemento
      // del padre.
      int posicionCicloActual=mother[0];

      //El ciclo es 2*numerodeCiudades ya que el ciclo puede tener a todas las ciudades, en caso de que
      //no tenga a todas las ciudades solo repite vertices del ciclo y los vuelve a marcar como true.
      for(int i=1;i<2*numerodeCiudades;i++){
        int moduloi=i%2;//Esta variable es para saber si estamos en un gen del padre o de la madre.
        if(moduloi==1){
          for(int j=0;j<numerodeCiudades;j++){
            //En este for buscamos la posicion donde el gen de la madre es igual al gen del padre
            //y la cambiamos a true para despues agregar ese gen al hijo.
            if(posicionCicloActual==father[j]){
                posicionCicloActual=j;
                CiudadesdelCiclo[j]=true;
                break;//al encontrarlo salimos del ciclo.
              }
           }
         }
        if(moduloi==0){
        posicionCicloActual=mother[posicionCicloActual];//guardamos el valor del gen de la madre para
              //despues buscarlo en el padre.
         }
       }

      //Este for es para agregar los genes del padre que estan el en ciclo y en caso contrario poner
      //los de la madre.
      for(int i=0;i<CiudadesdelCiclo.length;i++){

        if(CiudadesdelCiclo[i]){
          hijo[i]=father[i];
        }else{
          hijo[i]=mother[i];
        }
      }
          return hijo;
    }

    /** Hay muchas variantes de esta mutacion, la que vimos en clase es:
    * Eliges numero aleatorio y esa posicion la pones al final de la codificacion.
    *
    * @param tour El tour que sera mutado.
    * @return int[] El tour ya mutado.
    */
    public static int[] DisplacementMutation(int tour[]){
      int numerodeCiudades = tour.length;//Variable para saber numero de ciudades.
      int numeroAuxiliar1 = (int)(Math.random() * numerodeCiudades);//Creamos numero aleatorio para 
      //ponerlo al final del tour.
      int genaCambiar=tour[numeroAuxiliar1];
      for(int i=numeroAuxiliar1;i<numerodeCiudades-1;i++){
            //En este for recorremos los genes a la izquierda.
            tour[i]=tour[(i+1)%numerodeCiudades];
      }
      //Aqui ponemos al gen a cambiar al final del tour.
      tour[numerodeCiudades-1]=genaCambiar;
      return tour;
    }

    /**Hay muchas variaciones de esta mutacion, la que vimos en clase es:
    *
    * Eliges dos numeros aleatorios y cambias los genes en dicha posicion.
    *
    * @param tour El tour que sera mutado
    * @return int[] El tour despues de mutar.
    */

    public static int[] ExchangeMutation(int tour[]){
      int numerodeCiudades = tour.length;//Variable para saber numero de ciudades.
      int numeroAuxiliar1= (int)(Math.random() * numerodeCiudades);
      int numeroAuxiliar2= (int)(Math.random() * numerodeCiudades);
      //Aqui guardas los valores de los genes en las posiciones al azar que elejiste para luego cambiaras.
      int tour1=tour[numeroAuxiliar1];
      int tour2=tour[numeroAuxiliar2];
      //Aqui cambias los valores antes guardado en las variables.
      tour[numeroAuxiliar1]=tour2;
      tour[numeroAuxiliar2]=tour1;
      return tour;
     }

  public static void main(String[] args) {

    Constantes constante = new Constantes();
    int ciudades[][] = constante.llenado();
    AlgoritmoGenetico Red = new AlgoritmoGenetico(ciudades);//Inicializas un elemento de la clase
    //AlgoritmoGenetico.
    int numerodeciudades=Red.Matriz.length;//Variable que nos sirve para saber el numero de ciudades.
    System.out.println("Poblacion Inicial");
    int ListaPoblacion[][]=CrearPoblacion(Red.Matriz);//Creas la primer generacion.
    int numeroIteraciones =(int)(numerodeciudades/5);//variable que define cuantas iteraciones quieres
    //para mejorar tu solucion.
    //Este ciclo determina la cantidad de veces que repetimos el cruzamiento de padres y madres.

    while(0<numeroIteraciones){

      int numeroListaPoblacionAuxiliar=0;//Variable para saber si ListaPoblacionAuxiliar tiene los elementos
      //que deseamos.
      int ListaPoblacionAuxiliar[][]=new int[(int)(numerodeciudades/5)][numerodeciudades];//Este arreglo nos sirve
      //para generar una nueva lista despues de cruzar y mutar a padres y madres.
      int promediofitness=50000;//Variable que nos ayuda para saber el promedio
      int minimofitness=fitness(ListaPoblacion[0],Red);//variable que nos ayuda a saber quien tiene el menor fitness.
      // sin perdida de generalidad podemos inicializarlo como el fitness del primero y luego actualizar.

      while(numeroListaPoblacionAuxiliar<ListaPoblacionAuxiliar.length){

        int father[]=new int[ListaPoblacionAuxiliar[0].length];//Arreglo que nos sirve para elegir al padre.
        int mother[]=new int[ListaPoblacionAuxiliar[0].length];//Arreglo que nos sirve para elegir a la madre.
        int elementospadre=0;//Variable que nos sirve para saber que tenemos padre y madre.
        int elementosmadre=0;//Variable que nos sirve para saber que tenemos padre y madre.
        while(elementospadre<1){
          boolean fatherOcupado=false;
          int numeroTour= (int)(Math.random() * ListaPoblacionAuxiliar.length);//Numero al azar para elegir un tour de la poblacion.
          double numeroProbabilidadAptitud= Math.random();//Numero al azar para determnar si cruzamos o no al tour elegido aleatoriamente.
          //Con este if aseguramos que si tiene menor fitness entonces se cruce.
          if(fitness(ListaPoblacion[numeroTour%10],Red)==minimofitness && 
                    !fatherOcupado){
              father=ListaPoblacion[numeroTour%10];
              elementospadre=elementospadre+1;
              fatherOcupado=true;
              break;
          }
          //Esto asegura que los que estan abajo del promedio tengan mucha probabilidad de ser elegidos.
          if( fitness(ListaPoblacion[numeroTour%10],Red)<=promediofitness &&
                      !fatherOcupado && numeroProbabilidadAptitud>.3){

              father=ListaPoblacion[numeroTour%10];
              elementospadre=elementospadre+1;
              fatherOcupado=true;
              break;

          }

          //Esto asegura que los que estan por arriba del promedio tengan poca probabilidad de ser elegidos.
          if( fitness(ListaPoblacion[numeroTour%10],Red)>=promediofitness &&
                      !fatherOcupado && numeroProbabilidadAptitud<.3){

              father=ListaPoblacion[numeroTour%10];
              elementospadre=elementospadre+1;
              fatherOcupado=true;
              break;

            }
          }

          while(elementosmadre<1){

          boolean motherOcupado=false;
          int numeroTour= (int)(Math.random() * ListaPoblacionAuxiliar.length);//Numero al azar para elegir un tour de la poblacion.
          double numeroProbabilidadAptitud= Math.random();//Numero al azar para determnar si cruzamos o no al tour elegido aleatoriamente.
          //Con este if aseguramos que si tiene menor fitness entonces se cruce.
          if(fitness(ListaPoblacion[numeroTour%10],Red)==minimofitness && !motherOcupado ){
              mother=ListaPoblacion[numeroTour%10];
              elementosmadre=elementosmadre+1;
              motherOcupado=true;
              break;
        }

        //Esto asegura que los que estan abajo del promedio tengan mucha probabilidad de ser elegidos.
        if(fitness(ListaPoblacion[numeroTour%10],Red)<=promediofitness && 
                    numeroProbabilidadAptitud>.3 && !motherOcupado){

            mother=ListaPoblacion[numeroTour%10];
            elementosmadre=elementosmadre+1;
            motherOcupado=true;
            break;
        }
        //Esto asegura que los que estan por arriba del promedio tengan poca probabilidad de ser elegidos.
        if( fitness(ListaPoblacion[numeroTour%10],Red)>=promediofitness &&
                  !motherOcupado && numeroProbabilidadAptitud<.3){

          father=ListaPoblacion[numeroTour%10];
          elementosmadre=elementosmadre+1;
          motherOcupado=true;
          break;
        }
      }

      //Para este punto ya hemos elegido un father y una mother
      int numeroCruce = (int)(Math.random()*3)+2;//Elegimos un numero al azar 1,2 o 3 y mandamos
      //a llamar al cruce correspondiente.
      //System.out.print(numeroCruce);
      double numeroProbabilidadMutacion= Math.random();// Si el numero es menor a .2 entonces
      //hacemos la correspondiente mutacion.
      int numeroMutacion=(int)(Math.random()*2);//Elegimos un numero al azar 1 o 2  y mandamos
      //a llamar a la mutacion correspondiente.
      if(numeroCruce==1){
          int elementonuevo[]= PartiallyMappedCrossover(father,mother);//Aplicamos PartiallyMappedCrossover a nuestro elemento
          if(numeroProbabilidadMutacion<=.2 && numeroMutacion==1){
              elementonuevo=ExchangeMutation(elementonuevo);//Aplicamos ExchangeMutation a nuestro elemento.
              ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;//Agregamos al elemento obtenido.
              numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1
          }
          if(numeroProbabilidadMutacion<=.2 && numeroMutacion==2){
              elementonuevo=DisplacementMutation(elementonuevo);//Aplicamos DisplacementMutation a nuestro elemento.
              ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;//Agregamos al elemento obtenido.
              numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1.
          }
          ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;
          numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1.
        }
      //Hacemos el mismo proceso con la funcion OrderCrossover
      if(numeroCruce==2){
          int elementonuevo[]= OrderCrossover(father,mother);//Aplicamos OrderCrossover a nuestro elemento
        if(numeroProbabilidadMutacion<=.2 && numeroMutacion==1){
            elementonuevo=ExchangeMutation(elementonuevo);//Aplicamos ExchangeMutation a nuestro elemento.
            ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;//Agregamos al elemento obtenido.
            numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1
        }
        if(numeroProbabilidadMutacion<=.2 && numeroMutacion==2){

            elementonuevo=DisplacementMutation(elementonuevo);//Aplicamos DisplacementMutation a nuestro elemento.
            ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;//Agregamos al elemento obtenido.
            numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1.
        }

        ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;
        numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1.
      }
      //Hacemos el mismo proceso con la funcion CycleCrossover
      if(numeroCruce>=3){

            int elementonuevo[]= CycleCrossover(father,mother);//Aplicamos CycleCrossover a nuestro elemento
            if(numeroProbabilidadMutacion<=.2 && numeroMutacion==1){
              elementonuevo=ExchangeMutation(elementonuevo);//Aplicamos ExchangeMutation a nuestro elemento.
              ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;//Agregamos al elemento obtenido.
              numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1
          }
          if(numeroProbabilidadMutacion<=.2 && numeroMutacion==2){

              elementonuevo=DisplacementMutation(elementonuevo);//Aplicamos DisplacementMutation a nuestro elemento.
              ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;//Agregamos al elemento obtenido.
              numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1.
          }

          ListaPoblacionAuxiliar[numeroListaPoblacionAuxiliar%10]=elementonuevo;
          numeroListaPoblacionAuxiliar=numeroListaPoblacionAuxiliar+1;//Aumentamos la variable en 1.
      }

    }
    ListaPoblacion=ListaPoblacionAuxiliar;//Recuperamos la poblacion hasta esta iteracion antes de que se
    //vuelva a reinicializar ListaPoblacionAuxiliar.
    numeroIteraciones=numeroIteraciones-1;//Reducimos numero de iteraciones en 1 para que el ciclo
    //termine.
    }
  System.out.println("La poblacion Final es:");
  for(int i=0;i<ListaPoblacion.length;i++){
    System.out.print(i);
    System.out.print(":");
    for(int j=0;j<ListaPoblacion[0].length;j++){
      System.out.print(ListaPoblacion[i][j]);
      System.out.print(",");
    }
  System.out.print(" fitness:");
  System.out.print(fitness(ListaPoblacion[i],Red));
  System.out.println("");
  }
  }
}
