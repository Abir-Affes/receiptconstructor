package tn.idrm.receiptconstructor.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.idrm.receiptconstructor.domain.enumeration.result;
import tn.idrm.receiptconstructor.domain.enumeration.trans_type;

/**
 * A Receipt.
 */
@Entity
@Table(name = "receipt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Max(value = 9999)
    @Column(name = "receipt_no", unique = true)
    private Integer receipt_no;

    @Max(value = 999999)
    @Column(name = "trace_no")
    private Integer trace_no;

    @DecimalMax(value = "99999.99")
    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private trans_type transaction_type;

    @Column(name = "vu_no")
    private Integer vu_no;

    @Column(name = "receipt_type")
    private String receipt_type;

    @Column(name = "ref_parameters")
    private Integer ref_parameters;

    @Column(name = "licensing_no")
    private Integer licensing_no;

    @Column(name = "pos_info")
    private Integer pos_info;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private result result;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Terminal terminal_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Card card_id;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Receipt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReceipt_no() {
        return this.receipt_no;
    }

    public Receipt receipt_no(Integer receipt_no) {
        this.setReceipt_no(receipt_no);
        return this;
    }

    public void setReceipt_no(Integer receipt_no) {
        this.receipt_no = receipt_no;
    }

    public Integer getTrace_no() {
        return this.trace_no;
    }

    public Receipt trace_no(Integer trace_no) {
        this.setTrace_no(trace_no);
        return this;
    }

    public void setTrace_no(Integer trace_no) {
        this.trace_no = trace_no;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Receipt amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public trans_type getTransaction_type() {
        return this.transaction_type;
    }

    public Receipt transaction_type(trans_type transaction_type) {
        this.setTransaction_type(transaction_type);
        return this;
    }

    public void setTransaction_type(trans_type transaction_type) {
        this.transaction_type = transaction_type;
    }

    public Integer getVu_no() {
        return this.vu_no;
    }

    public Receipt vu_no(Integer vu_no) {
        this.setVu_no(vu_no);
        return this;
    }

    public void setVu_no(Integer vu_no) {
        this.vu_no = vu_no;
    }

    public String getReceipt_type() {
        return this.receipt_type;
    }

    public Receipt receipt_type(String receipt_type) {
        this.setReceipt_type(receipt_type);
        return this;
    }

    public void setReceipt_type(String receipt_type) {
        this.receipt_type = receipt_type;
    }

    public Integer getRef_parameters() {
        return this.ref_parameters;
    }

    public Receipt ref_parameters(Integer ref_parameters) {
        this.setRef_parameters(ref_parameters);
        return this;
    }

    public void setRef_parameters(Integer ref_parameters) {
        this.ref_parameters = ref_parameters;
    }

    public Integer getLicensing_no() {
        return this.licensing_no;
    }

    public Receipt licensing_no(Integer licensing_no) {
        this.setLicensing_no(licensing_no);
        return this;
    }

    public void setLicensing_no(Integer licensing_no) {
        this.licensing_no = licensing_no;
    }

    public Integer getPos_info() {
        return this.pos_info;
    }

    public Receipt pos_info(Integer pos_info) {
        this.setPos_info(pos_info);
        return this;
    }

    public void setPos_info(Integer pos_info) {
        this.pos_info = pos_info;
    }

    public result getResult() {
        return this.result;
    }

    public Receipt result(result result) {
        this.setResult(result);
        return this;
    }

    public void setResult(result result) {
        this.result = result;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Receipt date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Terminal getTerminal_id() {
        return this.terminal_id;
    }

    public void setTerminal_id(Terminal terminal) {
        this.terminal_id = terminal;
    }

    public Receipt terminal_id(Terminal terminal) {
        this.setTerminal_id(terminal);
        return this;
    }

    public Card getCard_id() {
        return this.card_id;
    }

    public void setCard_id(Card card) {
        this.card_id = card;
    }

    public Receipt card_id(Card card) {
        this.setCard_id(card);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Receipt)) {
            return false;
        }
        return id != null && id.equals(((Receipt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Receipt{" +
            "id=" + getId() +
            ", receipt_no=" + getReceipt_no() +
            ", trace_no=" + getTrace_no() +
            ", amount=" + getAmount() +
            ", transaction_type='" + getTransaction_type() + "'" +
            ", vu_no=" + getVu_no() +
            ", receipt_type='" + getReceipt_type() + "'" +
            ", ref_parameters=" + getRef_parameters() +
            ", licensing_no=" + getLicensing_no() +
            ", pos_info=" + getPos_info() +
            ", result='" + getResult() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
