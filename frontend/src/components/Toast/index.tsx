import useGenerationStatus from "@stores/useStatus";
import { Fragment, useEffect, useState } from "react"; import styles from "./index.module.scss";

export default function StatusToast() {
    const { generationStatus, setGenerationStatus } = useGenerationStatus();

    useEffect(() => {
        if (!generationStatus) return;
        const timer = setTimeout(() => {
            setGenerationStatus(undefined);
        }, 4000);

        return () => clearTimeout(timer);
    }, [generationStatus, setGenerationStatus]);

    if (!generationStatus) return null;

    return <div className={styles.toast}>{generationStatus}</div>;
  };
  