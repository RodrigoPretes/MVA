package src;

import java.util.Scanner;

class Queue {
    private double serviceTime; // Tempo médio de serviço (Si)
    private double visits;      // Número médio de visitas (Vi)
    private double avgCustomers; // Número médio de clientes (Ni)
    private double responseTime; // Tempo médio de resposta (Ri)
    private double waitingTime; // Tempo médio de resposta (Ri)
    private double utilization;  // Utilização do recurso (Ui)
    private double throughputQueue; // Vazão do dispositivo (Xi)
    
    public Queue(double serviceTime, double visits) {
        this.serviceTime = serviceTime;
        this.visits = visits;
        this.avgCustomers = 0.0;
    }
    
    public void calculateResponseTime(double prevAvgCustomers){
        this.responseTime = serviceTime * (1 + prevAvgCustomers);
    }
    
    public void calculateMetrics(double throughput) {
        this.throughputQueue = visits * throughput;
        this.utilization = serviceTime * throughputQueue;
        this.avgCustomers = responseTime * throughputQueue;
        this.waitingTime = responseTime - serviceTime;
    }
    
    public double getResponseTime() {
        return responseTime;
    }

    public double getAvgCustomers() {
        return avgCustomers;
    }
    
    public double getUtilization() {
        return utilization;
    }
    
    public double getVisits() {
        return visits;
    }
    public double getThroughputQueue() {
        return throughputQueue;
    }
    
    public void setThroughputQueue(double throughputQueue) {
        this.throughputQueue = throughputQueue;
    }
    public double getWaitingTime() {
        return waitingTime;
    }
    
    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }
}
    
    public class mva {
        private static Queue[] queues;
        private static int numQueues;
        private static int numCustomers;
    
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
    
            // Entradas do sistema
            System.out.print("Número de filas (recursos): ");
            numQueues = scanner.nextInt();
            queues = new Queue[numQueues];
    
            for (int i = 0; i < numQueues; i++) {
                System.out.println("\nConfiguração para a fila " + (i + 1) + ":");
                System.out.print("Tempo médio de serviço (Si): ");
                double serviceTime = scanner.nextDouble();
    
                System.out.print("Número médio de visitas (Vi): ");
                double visits = scanner.nextDouble();
    
                queues[i] = new Queue(serviceTime, visits);
            }
    
            System.out.print("\nNúmero total de clientes no sistema (N): ");
            numCustomers = scanner.nextInt();
    
            double[] throughput = new double[numCustomers + 1];
            throughput[0] = 1.0; // Inicialização para evitar divisão por zero
            
            double systemResponseTime = 0.0;
            // Loop para calcular as métricas para cada N
            for (int n = 1; n <= numCustomers; n++) {
                systemResponseTime = 0.0;
    
                for (Queue queue : queues) {
                    queue.calculateResponseTime(n == 1 ? 0 : queue.getAvgCustomers());
                    systemResponseTime += queue.getResponseTime() * queue.getVisits();
            }

            throughput[n] = n / systemResponseTime;

            for (Queue queue : queues) {
                queue.calculateMetrics(throughput[n]);
            }
        }

        // Exibir resultados
        System.out.println("\nResultados:");
        for (int i = 0; i < numQueues; i++) {
            Queue queue = queues[i];
            System.out.printf("\nFila %d:\n", i + 1);
            System.out.printf("Tempo médio de resposta (Ri): %.2f\n", queue.getResponseTime());
            System.out.printf("Tempo médio de Espera (Wi): %.2f\n", queue.getWaitingTime());
            System.out.printf("Número médio de clientes (Ni): %.2f\n", queue.getAvgCustomers());
            System.out.printf("Utilização (Ui): %.2f\n", queue.getUtilization());
        }

        System.out.println("\n");
        System.out.printf("Tempo médio de resposta total (Ro): %.2f\n", systemResponseTime);

        scanner.close();
    }
}
