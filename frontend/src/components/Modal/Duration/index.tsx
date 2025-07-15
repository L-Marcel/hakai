import { useState } from "react";
import styles from "./index.module.scss";
import Button from "@components/Button";

interface DurationModalProps {
    onClose: () => void;
    onSelectDuration: (minutes: number) => void;
}

export default function DurationModal({
    onClose,
    onSelectDuration,
}: DurationModalProps) {
    // 2. Criar um estado para armazenar o valor do input, com um valor padrão
    const [customMinutes, setCustomMinutes] = useState(10);

    const handleConfirm = () => {
        // Garante que o valor é um número positivo antes de enviar
        if (customMinutes > 0) {
            onSelectDuration(customMinutes);
        }
    };

    return (
        <div className={styles.overlay}>
            <div className={styles.modal}>
                <h2>Escolha a duração da sala</h2>

                {/* 3. Substituir os botões por um campo de input e um botão de confirmação */}
                <div className={styles.inputGroup}>
                    <input
                        type="number"
                        value={customMinutes}
                        onChange={(e) => setCustomMinutes(Number(e.target.value))}
                        className={styles.input}
                        min="1" // Impede que o usuário insira valores negativos ou zero
                        placeholder="Ex: 10"
                    />
                    <span>minutos</span>
                </div>

                <div className={styles.actions}>
                    <Button theme="light-red" onClick={onClose}>
                        Cancelar
                    </Button>
                    <Button onClick={handleConfirm}>
                        Confirmar
                    </Button>
                </div>
            </div>
        </div>
    );
}